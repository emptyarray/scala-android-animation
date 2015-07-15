package com.emptyarray.scala.android

import android.animation.IntEvaluator
import android.view.animation.{DecelerateInterpolator, OvershootInterpolator}
import com.emptyarray.scala.android.util.ThreadPoolExecutionContext._
import com.emptyarray.scala.android.util.{ViewHelper, UiThreadHelper}
import org.scaloid.common._

import scala.concurrent.Future
import com.emptyarray.scala.android.util.ViewHelper._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.async.Async.{async, await}

class RefreshActivity extends SActivity with TypedFindView with UiThreadHelper {
  private lazy val refreshButton = findView(TR.refreshButton)
  private lazy val progress = findView(TR.progress)
  private lazy val accountBalance = findView(TR.balance)

  val startBalance = 10

  val LOG_TAG = getClass.getName

  onCreate {
    setContentView(R.layout.refresh)

    refreshButton.onClick(refresh())
    accountBalance.setText("$10")

  }

  // fake an immediate succesful HTTP response
  def getBalance() = Future.successful(99)

  def refresh(): Unit = {

    async {
      await(refreshButton.animateAlpha(0.5f, 500 millis))
      onUiThread { refreshButton.setEnabled(false) }

      val fakeHttpRequest = getBalance()

      await(progress.animateAlpha(1f, 500 millis))

      // asynchronously wait for the new balance from the server
      val newBalance: Int = await(fakeHttpRequest)

      await(accountBalance.animateScale(0.8f, 0.8f, 300 millis))

      val numberAnimation = ViewHelper.valueAnimator(
        startBalance,
        newBalance,
        1.5 seconds,
        new DecelerateInterpolator(1),
        new IntEvaluator()
      ) {
        v => accountBalance.setText("$" + v.toString)
      }

      await(numberAnimation)
      await(accountBalance.animateScale(1f, 1f, 500 millis, new OvershootInterpolator(3f)))
      await(progress.animateAlpha(0f, 500 millis))
      await(refreshButton.animateAlpha(1f, 500 millis))

      onUiThread { refreshButton.setEnabled(true) }
    }

  }

}


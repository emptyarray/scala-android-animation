package com.emptyarray.scala.android.util

import android.os.{Handler, Looper}

import scala.concurrent.{Future, ExecutionContext}
import scala.util.{Failure, Success, Try}

/**
 * Created by Weasel on 7/7/15.
 */
object UiThreadExecutionContext extends ExecutionContext {
  private lazy val uiHandler = new Handler(Looper.getMainLooper)

  def reportFailure(t: Throwable) = t.printStackTrace()
  def execute(runnable: Runnable) = {
    if(Looper.myLooper() == Looper.getMainLooper) runnable.run() else uiHandler.post(runnable)
  }
}

trait UiThreadHelper {
  def onUiThread(f: => Any): Any = {
    onUiThread(new Runnable() {
      override def run() = f
    })
  }

  def onUiThread(runnable: Runnable) = UiThreadExecutionContext.execute(runnable)

  def getFromUiThread[A](f: () => A): Future[A] =
    if(Looper.myLooper() == Looper.getMainLooper) {
      Try(f()) match {
        case Success(x) => Future.successful(x)
        case Failure(x) => Future.failed(x)
      }
    }
    else {
      Future(f())(UiThreadExecutionContext)
    }

}
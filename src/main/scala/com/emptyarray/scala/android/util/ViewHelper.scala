package com.emptyarray.scala.android.util

import android.animation.ValueAnimator.AnimatorUpdateListener
import android.animation._
import android.util.Log
import android.view.View
import android.view.animation.{AccelerateDecelerateInterpolator, Interpolator}

import scala.concurrent.{Promise, Future}
import scala.concurrent.duration.FiniteDuration
import ThreadPoolExecutionContext._

/**
 * Created by Weasel on 7/6/15.
 */
object ViewHelper extends UiThreadHelper {

  implicit class AnimatedView(v: View) {
    def animateAlpha(alpha: Float, duration: FiniteDuration): Future[Unit] = {

      getFromUiThread(v.getAlpha).flatMap {
        startAlpha =>
          ViewHelper.valueAnimator(startAlpha, alpha, duration,
            new AccelerateDecelerateInterpolator(), new FloatEvaluator())(v.setAlpha)
      }
    }

    def animateScale(scaleX: Float, scaleY: Float, duration: FiniteDuration,
                     interpolator: Interpolator = new AccelerateDecelerateInterpolator()): Future[Unit] = {
      getFromUiThread( () => (v.getScaleX, v.getScaleY) ).flatMap {
        case (x, y) =>
          val startX = x
          val startY = y

          ViewHelper.valueAnimator(0f, 1f, duration,
            interpolator, new FloatEvaluator()) {
            fraction =>
              v.setScaleX(startX + (scaleX - startX) * fraction)
              v.setScaleY(startY + (scaleY - startY) * fraction)
          }
      }
    }
  }

  // animate an arbitary function f in a ValueAnimator
  def valueAnimator[A, T](start: A, stop: A, duration: FiniteDuration,
                       interpolator: TimeInterpolator,
                       evaluator: TypeEvaluator[T])
                      (f: A => Unit): Future[Unit] = {
    val animator = (start, stop) match {
      case (s: Float, e: Float) => ValueAnimator.ofFloat(s, e)
      case (s: Int, e: Int) => ValueAnimator.ofInt(s, e)
    }

    animator.setDuration(duration.toMillis)
    animator.setInterpolator(interpolator)
    animator.setEvaluator(evaluator)

    animator.addUpdateListener(new AnimatorUpdateListener {
      override def onAnimationUpdate(animator: ValueAnimator): Unit = {
        f(animator.getAnimatedValue.asInstanceOf[A])
      }
    })

    runAnimator(animator)
  }

  def runAnimator(animator: Animator): Future[Unit] = {
    val p = Promise[Unit]()

    animator.addListener(new AnimatorListenerAdapter {
      override def onAnimationEnd(animator: Animator): Unit = p.success(())
      override def onAnimationCancel(animtor: Animator): Unit = p.success(())
    })

    // have to run the animator from the UI thread
    onUiThread(animator.start())

    p.future
  }
}

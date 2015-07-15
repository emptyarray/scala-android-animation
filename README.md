# Advanced Android Animation in Scala

```scala
async {
  await(refreshButton.animateAlpha(0.5f, 500 millis))
  onUiThread { refreshButton.setEnabled(false) }

  val fakeHttpRequest = getBalance()

  await(progress.animateAlpha(1f, 500 millis))

  // asynchronously wait for the new balance from the server
  val newBalance: Int = await(fakeHttpRequest)

  await(accountBalance.animateScale(0.8f, 0.8f, 300 millis))

  // animate the dollar amount
  val numberAnimation = ViewHelper.valueAnimator(
    startBalance,
    newBalance, // the new balance from the server
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
```

- [Full tutorial](http://emptybrackets.com/)
- [Project template by 47deg](http://www.47deg.com/blog/scala-on-android-preparing-the-environment)
- Inspired by [Macroid](http://macroid.github.io/)

## License

MIT
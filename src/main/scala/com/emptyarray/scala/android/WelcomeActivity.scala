package com.emptyarray.scala.android

import android.os.{AsyncTask, Bundle}
import android.util.Log
import android.view.View
import com.emptyarray.scala.android.models.User
import org.json.JSONObject
import org.scaloid.common._

import scala.concurrent.{Future, ExecutionContext}
import scala.util.Try
import com.emptyarray.scala.android.util.ViewHelper._
import scala.concurrent.duration._
import scala.language.postfixOps

class WelcomeActivity extends SActivity with TypedFindView  {
  private lazy val loginButton = findView(TR.loginButton)
  private lazy val email = findView(TR.email)
  private lazy val password = findView(TR.password)

  val LOG_TAG = getClass.getName

  onCreate {
    setContentView(R.layout.main)

    loginButton.onClick(login())
  }

  def login(): Unit = {

  }

}


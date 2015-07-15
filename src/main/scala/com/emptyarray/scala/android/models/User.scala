package com.emptyarray.scala.android.models

import android.os.AsyncTask
import org.json.JSONObject

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

/**
 * Created by Weasel on 6/28/15.
 */

case class User(id: Int, name: String) {
  val posts = Array.empty[Post]

  implicit val execContext = ExecutionContext.fromExecutor( AsyncTask.THREAD_POOL_EXECUTOR )

  def findPosts(): Future[Array[Post]] = {
    val mockPosts = Array(1, 2).map {
      x => Post(x, 1, s"Post $x")
    }
    Future.successful(mockPosts)
  }

  def deleteAllPosts():Future[Seq[Int]] = {
    val deletions: Seq[Future[Int]] = posts.map(_.delete()).toSeq

    Future.sequence(deletions)
  }
}

object User {
  def find(id: Int): Future[User] = Future.successful(User(1, "Mock User"))
  def findByEmail(email: String): Future[User] = Future.successful(User(1, "Mock User"))
}

case class Post(id: Int, userId: Int, title: String) {
  def delete(): Future[Int] = Future.successful(id)
}



object UpdateMessage {
  def apply(json: JSONObject): Unit = {
    val userId = Try(json.getInt("user_id")).toOption
    val commentCount = Try(json.getInt("comment_count")).toOption
    val subsciberCount = Try(json.getInt("subscriber_count")).toOption

    UpdateMessage(userId, commentCount, subsciberCount)
  }
}

case class UpdateMessage(userId: Option[Int], commentCount: Option[Int], subscriberCount: Option[Int])

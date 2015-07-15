package com.emptyarray.scala.android.util

import android.os.AsyncTask

import scala.concurrent.ExecutionContext

/**
 * Created by Weasel on 7/7/15.
 */
object ThreadPoolExecutionContext {
  implicit val execContext = ExecutionContext.fromExecutor( AsyncTask.THREAD_POOL_EXECUTOR )
}

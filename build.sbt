import android.Keys._

android.Plugin.androidBuild

// Specifying the Android target Sdk version
platformTarget in Android := "android-22"

// Application Name
name := """scala-android"""

// Application Version
version := "1.0.0"

// Scala version
scalaVersion := "2.11.6"

// Repositories for dependencies
resolvers ++= Seq(Resolver.mavenLocal,
  DefaultMavenRepository,
  Resolver.typesafeRepo("releases"),
  Resolver.typesafeRepo("snapshots"),
  Resolver.typesafeIvyRepo("snapshots"),
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.defaultLocal)

// Override the run task with the android:run
run <<= run in Android

proguardScala in Android := true

useProguard in Android := true

proguardOptions in Android ++= Seq(
  "-dontobfuscate", "-dontoptimize",
  "-keep class scala.Dynamic")

proguardCache in Android ++= Seq(
  "org.scaloid"
)

scalacOptions in Compile += "-feature"

install <<= install in Android

libraryDependencies ++= Seq(
  "org.scaloid" %% "scaloid" % "3.6.1-10",
  "org.scala-lang.modules" % "scala-async_2.11" % "0.9.4"
)


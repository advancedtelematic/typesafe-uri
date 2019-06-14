// *****************************************************************************
// Projects
// *****************************************************************************

import ReleaseTransformations._
import sbtrelease.ExtraReleaseCommands
import java.net.URI

def readSettings(envKey: String, propKey: Option[String] = None): String = {
  sys.env.get(envKey).orElse(propKey.flatMap(sys.props.get(_))).getOrElse("")
}

val nexus = readSettings("PUBLISH_URL")
lazy val `typesafe-uri` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin, GitVersioning)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.parboiled,
        library.scalaCheck % Test,
        library.scalaTest % Test
      )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {

    object Version {
      val scalaCheck = "1.13.4"
      val scalaTest = "3.0.1"
      val parboiled = "2.1.4"
    }

    val scalaCheck = "org.scalacheck" %% "scalacheck" % Version.scalaCheck
    val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
    val parboiled = "org.parboiled" %% "parboiled" % Version.parboiled
  }

// *****************************************************************************
// Settings
// *****************************************************************************        |

lazy val settings =
  commonSettings ++
    gitSettings ++
    headerSettings

lazy val commonSettings =
  Seq(
    // scalaVersion and crossScalaVersions from .travis.yml via sbt-travisci
    scalaVersion := "2.12.2",
    crossScalaVersions := Seq(scalaVersion.value, "2.11.11"),
    organization := "com.advancedtelematic",
    licenses += ("Apache 2.0",
      url("http://www.apache.org/licenses/LICENSE-2.0")),
      scmInfo := Some(
        ScmInfo(
          url("https://github.com/advancedtelematic/typesafe-uri"),
          "scm:git:git@github.com:advancedtelematic/typesafe-uri.git"
        )
      ),
    mappings.in(Compile, packageBin) += baseDirectory.in(ThisBuild).value / "LICENSE" -> "LICENSE",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding",
      "UTF-8", // yes, this is 2 args
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint:-missing-interpolator",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Xfuture",
      "-Ywarn-unused-import",
      "-target:jvm-1.8"
    ),
    javacOptions ++= Seq(
      "-source", "1.8",
      "-target", "1.8"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value),
      publishTo := {
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "content/repositories/releases")
    },
    credentials +=
      Credentials(
        readSettings("PUBLISH_REALM"),
        URI.create(nexus).getHost,
        readSettings("PUBLISH_USER"),
        readSettings("PUBLISH_PASSWORD")
      ),
    releaseProcess := Seq(
      checkSnapshotDependencies,
      releaseStepCommand(ExtraReleaseCommands.initialVcsChecksCommand),
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

import de.heikoseeberger.sbtheader.HeaderPattern
import de.heikoseeberger.sbtheader.license._

lazy val headerSettings =
  Seq(
    headers := Map("scala" -> (HeaderPattern.cStyleBlockComment,
      """|/*
         | * Copyright 2009-2017 Lightbend Inc. <http://www.lightbend.com>
         | * Copyright 2017 ATS Advanced Telematic Systems GmbH
         | *
         | * Licensed under the Apache License, Version 2.0 (the "License");
         | * you may not use this file except in compliance with the License.
         | * You may obtain a copy of the License at
         | *
         | *    http://www.apache.org/licenses/LICENSE-2.0
         | *
         | * Unless required by applicable law or agreed to in writing, software
         | * distributed under the License is distributed on an "AS IS" BASIS,
         | * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         | * See the License for the specific language governing permissions and
         | * limitations under the License.
         | */
         |
         |""".stripMargin))
  )

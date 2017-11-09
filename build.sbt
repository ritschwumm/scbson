import spray.boilerplate.BoilerplatePlugin

inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.131.0",
	
	scalaVersion	:= "2.12.4",
	scalacOptions	++= Seq(
		"-deprecation",
		"-unchecked",
		"-language:implicitConversions",
		"-language:existentials",
		// "-language:higherKinds",
		// "-language:reflectiveCalls",
		// "-language:dynamics",
		// "-language:postfixOps",
		// "-language:experimental.macros"
		"-feature",
		"-Xfatal-warnings",
		"-Xlint"
	),
	conflictManager	:= ConflictManager.strict,
	resolvers		+= "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
))

lazy val wartRemoverSetting	=
		wartremoverErrors	++= Seq(
			Wart.StringPlusAny,
			Wart.EitherProjectionPartial,
			Wart.OptionPartial,
			Wart.Enumeration,
			Wart.FinalCaseClass,
			Wart.JavaConversions,
			Wart.Option2Iterable,
			Wart.TryPartial,
			Wart.JavaSerializable,
			//Wart.Any,
			Wart.AnyVal,
			//Wart.Nothing,
			Wart.ArrayEquals,
			Wart.ExplicitImplicitTypes,
			Wart.LeakingSealed
			//Wart.Overloading
			//Wart.PublicInference,
			//Wart.TraversableOps
		)
		
lazy val `scbson` =
		(project in file("."))
		.aggregate(
			`scbson-ast`,
			`scbson-pickle`
		)
		.settings(
			publishArtifact := false
			//publish		:= {},
			//publishLocal	:= {}
		)
		
lazy val `scbson-ast`	=
		(project	in	file("sub/ast"))
		.settings(
			wartRemoverSetting,
			libraryDependencies	++= Seq(
				"de.djini"			%%	"scutil-base"	% "0.126.0"				% "compile"
			)
		)
		
lazy val `scbson-pickle`	=
		(project	in	file("sub/pickle"))
		.enablePlugins(
			BoilerplatePlugin
		)
		.dependsOn(
			`scbson-ast`
		)
		.settings(
			wartRemoverSetting,
			libraryDependencies	++= Seq(
				"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
				"de.djini"			%%	"scutil-base"	% "0.126.0"				% "compile"
			),
			boilerplateSource in Compile := baseDirectory.value/ "src" / "main" / "boilerplate"
		)
		
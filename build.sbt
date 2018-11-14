import spray.boilerplate.BoilerplatePlugin

inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.157.0",

	scalaVersion	:= "2.12.7",
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
	resolvers		+= "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",

	wartremoverErrors	++= Seq(
		Wart.AsInstanceOf,
		Wart.IsInstanceOf,
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
))

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
		(project	in	file("modules/ast"))
		.settings(
			libraryDependencies	++= Seq(
				"de.djini"			%%	"scutil-base"	% "0.148.0"				% "compile"
			)
		)

lazy val `scbson-pickle`	=
		(project	in	file("modules/pickle"))
		.enablePlugins(
			BoilerplatePlugin
		)
		.dependsOn(
			`scbson-ast`
		)
		.settings(
			libraryDependencies	++= Seq(
				// TODO could this be a provided dependency?
				// TODO is this dependency necessary at all?
				"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
				"de.djini"			%%	"scutil-base"	% "0.148.0"				% "compile"
			),
			boilerplateSource in Compile := baseDirectory.value/ "src" / "main" / "boilerplate"
		)

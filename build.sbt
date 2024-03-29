import spray.boilerplate.BoilerplatePlugin

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.225.0",

	scalaVersion	:= "2.13.6",
	scalacOptions	++= Seq(
		"-feature",
		"-deprecation",
		"-unchecked",
		"-Werror",
		"-Xlint",
		"-Xsource:3",
	),

	versionScheme	:= Some("early-semver"),
	conflictManager	:= ConflictManager.strict withOrganization "^(?!(org\\.scala-lang|org\\.scala-js)(\\..*)?)$",

	wartremoverErrors	++= Seq(
		Wart.AsInstanceOf,
		Wart.IsInstanceOf,
		Wart.StringPlusAny,
		Wart.ToString,
		Wart.EitherProjectionPartial,
		Wart.OptionPartial,
		Wart.TryPartial,
		Wart.Enumeration,
		Wart.FinalCaseClass,
		Wart.JavaConversions,
		Wart.Option2Iterable,
		Wart.JavaSerializable,
		//Wart.Any,
		Wart.AnyVal,
		//Wart.Nothing,
		Wart.ArrayEquals,
		Wart.ImplicitParameter,
		Wart.ExplicitImplicitTypes,
		Wart.LeakingSealed,
		Wart.DefaultArguments,
		Wart.Overloading,
		//Wart.PublicInference,
		Wart.TraversableOps
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
			"de.djini"			%%	"scutil-core"	% "0.210.1"				% "compile"
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
			"de.djini"			%%	"scutil-core"	% "0.210.1"				% "compile"
		),
		Compile / boilerplateSource	:= baseDirectory.value/ "src" / "main" / "boilerplate"
	)

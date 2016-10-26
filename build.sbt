name			:= "scbson"
organization	:= "de.djini"
version			:= "0.91.0"

scalaVersion	:= "2.11.8"
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
	"-Ywarn-unused-import",
	"-Xfatal-warnings",
	"-Xlint"
)

(sourceGenerators in Compile)	+=
		(Def.task {
			Boilerplate generate (sourceManaged in Compile).value
		}).taskValue

conflictManager	:= ConflictManager.strict
libraryDependencies	++= Seq(
	"de.djini"			%%	"scutil-core"	% "0.91.0"				% "compile",
	"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile"
)

wartremoverErrors ++= Seq(
	Wart.Any2StringAdd,
	Wart.EitherProjectionPartial,
	Wart.OptionPartial,
	Wart.Enumeration,
	Wart.FinalCaseClass,
	Wart.JavaConversions,
	Wart.Option2Iterable,
	Wart.TryPartial
)

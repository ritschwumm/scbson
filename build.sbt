name			:= "scbson"

organization	:= "de.djini"

version			:= "0.54.0"

scalaVersion	:= "2.11.2"

scalacOptions	++= Seq(
	"-deprecation", 
	"-unchecked",
	"-language:implicitConversions",
	"-language:existentials",
	// "-language:higherKinds",
	// "-language:reflectiveCalls",
	// "-language:dynamics",
	"-language:postfixOps",
	// "-language:experimental.macros"
	"-feature"
)

(sourceGenerators in Compile)	<+= (sourceManaged in Compile) map Boilerplate.generate

conflictManager	:= ConflictManager.strict

libraryDependencies	++= Seq(
	"de.djini"			%%	"scutil-core"	% "0.54.0"				% "compile",
	"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile"
)

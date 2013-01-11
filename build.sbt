name			:= "scbson"

organization	:= "de.djini"

version			:= "0.11.0"

scalaVersion	:= "2.10.0"

libraryDependencies	++= Seq(
	"de.djini"		%%	"scutil"	% "0.15.0"	% "compile"
)

libraryDependencies	<+= (scalaVersion) { "org.scala-lang" % "scala-reflect" % _ }

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

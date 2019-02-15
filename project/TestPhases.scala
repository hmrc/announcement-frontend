import sbt.{ForkOptions, TestDefinition, Tests}
import sbt.Tests.{Group, SubProcess}

object TestPhases {
  
  def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Tests.Group] =
    tests map {
      test => new Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
    }

}

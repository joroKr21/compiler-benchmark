import shapeless._
import ops.hlist._

object benchmarks {
  case class A(
    f1: Byte,
    f2: Short,
    f3: Int,
    f4: Long,
    f5: Float,
    f6: Double,
    f7: Char,
    f8: String,
    f9: Symbol
  )

  // // Generic, 9 fields, identical
  // val gen = Generic[A]
  // Intersection[gen.Repr, gen.Repr]

  // // LabelledGeneric, 9 fields, identical
  // val gen = LabelledGeneric[A]
  // Intersection[gen.Repr, gen.Repr]
  
  // // Generic, 9 fields, reversed
  // val gen = Generic[A]
  // val rev = Reverse[gen.Repr]
  // Intersection[gen.Repr, rev.Out]

  // LabelledGeneric, 9 fields, reversed
  val gen = LabelledGeneric[A]
  val rev = Reverse[gen.Repr]
  Intersection[gen.Repr, rev.Out]
}

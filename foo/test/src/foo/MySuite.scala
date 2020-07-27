package foo

import weaver._

object suite1 extends SimpleIOSuite {

  test("good 1") { expect(1 + 1 == 2) }

  test("bad 1") { expect(1 * 1 == 2) }

}

object suite2 extends SimpleIOSuite {

  test("good 2") { expect(2 + 2 == 4) }

  test("bad 2") { expect(2 * 2 == 5) }

}

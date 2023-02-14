package com.petitcl.domain4k.fixtures

import com.petitcl.domain4k.stereotype.DomainEvent

class A
class B

context(A)
fun doA() = "contextA"

context(B)
fun doB() = "contextB"

data class TestEvent(val data: String) : DomainEvent

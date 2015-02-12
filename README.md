Manifold is a high level language for creating all kinds of systems the way we
write software.

Currently, it focuses on hardware and microfludics design.

# Introduction

In much the same way that computer programs are written in programming
languages, digital circuits is "written" in a **hardware description language**.

The two hardware languages in wide use today are **VHDL** and **Verilog**. Both
were designed in the early 1980's and both have remained frozen in time since,
while our understanding of how to build expressive, powerful, and usable
programming languages has grown considerably. These languages are in widespread
use because they are the best tool for the job but they aren't nearly as
expressive or approachable as modern programming languages.

More recently, several projects have spring up that provide tooling to use
existing *programming* languages as hardware languages. These projects include
**[CλaSH](http:#clash.ewi.utwente.nl/ClaSH/Home.html)**,
**[MyHDL](http:#www.myhdl.org/doku.php)**, and many others. This approach has
not gained widespread use because programming languages are not true to the
underlying domain of hardware programming (see [The Challenges of Hardware
Synthesis from C-like Languages](http:#www1.cs.columbia.edu/~sedwards/papers/edw
ards2005challenges.pdf)).

Manifold is a reimagining of a hardware design language that remains true to the
underlying domain, like VHDL and Verilog, while also leveraging 30 years of
improved language design, like CλaSH and MyHDL. 

Manifold doesn't only do digitial circuits -- it can be extended to design all
kinds of systems, including analog circuits, microfluidics, and mechanical
systems.

You should use manifold becase

 - it allows you to design circuits, microfluidics, and other systems in an elegant and consistent way
 - it allows you to express solutions to hard problems in simple text files
 - it allows you to encapsulate these solutions within "modules"
 - it allows you to reuse these modules within your own projects
 - it allows you to share modules between projects, people, and organizations
 - it allows you to leverage the ecosystem of poweful software development tools

# Philosophy

**The compiler should work hard so that you don't have to.** (But hopefully the
compiler can be as elegant as the code it compiles!) Prefer implicit
specification over explicit. Provide smart defaults and painless overrides. Do
as much work as possible at compile time. Allow developers to express domain
logic as naturally as possible and let the compiler decide how to implement it. 

**Optimize for readability over writeability.** Developers spend 15 hours reading
code and 5 hours modifying code for every 1 hour of writing code. Avoid
confusing abbreviations or acronyms. Enforce part-of-speech naming conventions.
Prefer longer, descriptive, unambiguous names (within reason). Code should be as
self-documenting as possible. Brevity is best for both readability and
writeability.

**Be true to the underlying domain.** Borrow concepts from sequential programming
where possible but avoid inaccurate polysonomy. Expose all hardware primitives
available as naturally as possible.

**Build powerful self-hosted abstractions atop a minimal set of primitives.**
Provide a powerful macro system to create the illusion of complex language
features. Build smart abstractions around complexity.

# Terminology, Notation and Conventions

## Naming Conventions

In order to make Manifold readable and logically consistent, naming conventions
are enforced by a linguistic linter that runs, by default, during every
compilation (*not implemented yet*).

In particular, you are required to

 - avoid all acronyms and abbreviations that are not taught in introductory computer science courses, except where doing so becomes awkward or defies strong convention 
 - write type names in `UpperCamelCase` and all other names in `lowerCamelCase`
 - always use the same word to refer to the same idea and different words to refer to different ideas (avoid confusing polysemy and unnecessary synonymy)
 - Prefer verb phrases for function names and noun phrases for all other names, except where doing so becomes awkward or defies strong convention
 - Prefer one word names to two word names, two word names to three word names, etc.

# Frontend Language

The front end language is what most users interact with most of the time. This
is the language used for designing real systems with Manifold.

## Variables and Expressions

*TODO basic introduction*

## Booleans

The most fundamental type in Manifold is the `manifold.Boolean` type. A
`manifold.Boolean` represents a single bit of information: true or false,
represented as `manifold.true` or `manifold.false`. For example, we might turn
on our time machine by setting

```
manifold.Boolean timeMachineActivated = manifold.true
```

The compiler can infer the type of the variable so it is equivalent to write

```
timeMachineActivated = manifold.true
```

## Compiletime vs Runtime

In Manifold, you write domain logic as naturally as possible and let the
compiler decide how to represent that logic in hardware.

To this end, almost any expression in Manifold can be evaluated either

 - on the sequential processor where the Manifold code is being compiled, at **compiletime**
 - on the physical hardware, at **runtime**

Certain operations, of course, can *only* be executed at a specific "time" --
For example, top level io ports may *only* be read at runtime. Referencing an
external file in the compilation environment may only happen at compiletime.

Manifold is designed so that you don't need to think about the difference
between these two types of operations but may take control over them, if
desired.

## Tuples

A tuple is an ordered set of values that can be passed around as one logical
entity. Tuples are the glue that allow us to build domain objects -- like
numbers, genomes, and time machines -- out of `manifold.Boolean`s and other
primitive types.

For example, suppose you are describing the input to some hardware for a time
machine that can travel to any year with an optional invisibility shield. You
could define a tuple which groups and names these variables,

```
(year: manifold.Integer, invisibility: manifold.Boolean) input
```

create such a tuple,

```
input = (year: 5000, invisibility: manifold.true)
```

and access the properties in that tuple

```
input.invisibility # => 500
input.year         # => true
```

### Default Properties

The declaration may also include a default value for any property. 

```
(year: manifold.Integer = 3000, invisible: manifold.Boolean = manifold.false)
```

Any property which does not have a default value is required; any property which
does have a default value is optional.

### Positional Properties

In addition to named properties, tuples may have implicitly named positional
properties; these properties are named by their position in the tuple. For
example, an ordered pair (x,y) might be represented as

```
(manifold.Integer, manifold.Integer)
```

where the first `manifold.Integer` is implicitly named `0` and the second `1`.

```
(manifold.Integer, manifold.Integer) position = (2, 4)
position.0  # => 2
position.1  # => 4
```

### Repeated Positional Properties (Arrays)

"Arrays in Manifold are a special case of tuples which have many positional
"properties of a particular type. For example, an integer array of width 3 could
"be defined as

```
(manifold.Integer, manifold.Integer, manifold.Integer) array = (1, 2, 3)
```

or, with the equivalent shorthand,

```
(manifold.Integer...3) array = (1, 2, 3)
```

Sometimes it makes more sense to set the width of a tuple using a compiletime
evaluable expression. This often increases the readability and maintainability
of code.

```
manifold.Integer width = 5
(manifold.Integer...width) array = (1, 2, 3, 4, 5)
```

### Inferred Width Repeated Positional Properties

It is also possible to statically infer the width of a tuple at compiletime from
the value being passed to it. Simply reference a named property within the same
tuple of type `manifold.Integer` instead of a width expression.

```
(manifold.Integer...width, width: manifold.Integer) array = (0, 1, 2, 3, 4, 5)
array.1      # => 1
array.width  # => 5
```

### Subscript Operator

*TODO This is super speculative. Do we even want to include this in the spec right now?*

```
Type TimeMachineSettings = (year: year, invisible: invisible)
TimeMachineSettings settings = (year: 5, invisible: high)

settings[TimeMachineSettings.Property.year]

TimeMachineSettings.Property property = TimeMachineSettings.Property.year
settings[property]
```

### Destructuring Assignment

You may use destructuring assignment to extract the values from a tuple into
individual variables.

```
(year: manifold.Integer year, invisible: manifold.Boolean invisible) = (year: 5000, invisible: false)
```

This statement creates, in the local scope, the variables `year` and
`invisible`, assigned to `input.year` and `input.invisible` respectively.

Types in destructuring can be inferred by the compiler. It is functionally
equivalent to omit the types and write

```
(year: year, invisible: invisible) = (year: 5000, invisible: false)
```

Destructuring assignment need not extract all properties within a tuple. If a
you only need the destination year of your time machine, you need only write

```
(year: year) = (year: 5000, invisible: false)
```

### Casting

If a tuple is cast to another tuple type with additional properties and a
default value is provided for each of those properties, the cast will happen
successfully, using those default values.

```
(manifold.Integer...6 = 0) array = (1, 2, 3, 4)
# Array will have the value (1, 2, 3, 4, 0, 0)

(year: manifold.Integer = 5, invisibility: manifold.Boolean = false) settings = (year: 5)
# Settings will have the value (year: 5, invisibility: false)
```

If a tuple is cast to another tuple type with missing *named* properties, the
cast will happen successfully

```
(year: manifold.Integer) settings = (year: 5, invisibility: false)
# Settings will have the value (year: 5)
```

If a tuple is cast to another tuple type with missing *positional* properties,
there will be a compiletime error

```
# Causes compiletime TupleCastIllegalException
(manifold.Integer...3 = 0) array = (1, 2, 3, 4)
```

## Integers

*TODO Can we please represent integers in terms of tuples??*

## Enums

*TODO*

```
Enum TrafficLightState = {on, off, blinking}
TrafficLightState state = TrafficLightState.on
```

```
Enum TrafficLightDelay = {short: 5, long: 20}

TrafficLightDelay dely1 = TrafficLightDelay.short
TrafficLightDelay dely2 = TrafficLightDelay.medium # compiletime error
TrafficLightDelay delay3 = 5
TrafficLightDelay delay4 = 10 # compiletime error
manifold.Integer delay5 = TrafficLightDelay.short
```

```
Enum Bar = {foo: (0, name: 1), baz: (5)} # compiletime error
```

```
Enum Bar = {foo: (0, name: 1), baz: (5, name: 10)}

Bar bar = Bar.baz
bar.name
bar.0
Bar.baz[0]
Bar.baz.name
```

## Functions

A function is an entity that, given an input value, uses some logic to produce
an output value.

Suppose you were to write a function that determined if an input to the time
machine was unsafe (ie targeting a year after the robot uprising and without the
invisibility shield). This function takes in some input in the form `(year:
manifold.Integer, invisible: manifold.Boolean invisible)` and produces some
output in the form of a `manifold.Boolean`.

```
TimeMachineInput = (year: manifold.Integer, invisible: manifold.Boolean invisible)

Function isDangerous = TimeMachineInput -> manifold.Boolean dangerous {
  dangerous = year > 5000 and !invisible
}
```

Note in the above function that there is no `return` statement -- output
variables defined in the function definition are assigned to directly.

This function could be invoked as
```
settings = (year: 10000, invisibility: manifold.false)
dangerous = isDangerous settings
```

Multiple functions may be chained together as
```
if (!isDangerous settings) {
  travel startFluxCapacitor configureTimeCrystals settings
}
```

where the returned value of `configureTimeCrystals` is passed to
`startFluxCapacitor` and that of `startFluxCapacitor` is passed to `travel`.

### Stateful Functions

Functions in Manifold are not "pure" -- they may have state. For example, the
count function supplied by the core library can be used to track system uptime
in seconds as

```
manifold.Integer uptime = manifold.count(clock(hz: 1))
```
This function can "remember" values from clock tick to clock tick 

All state in Manifold is derived from the `manifold.recall` primitive function
```
Function manifold.recall = (T next, default: T) -> T current {...}
```

This primitive acts like a hardware flipflop in that the output value `current`
will always take on the value that the input `next` had during the previous
clock tick. As an example, we could implement a simple counter as

```
manifold.Integer ticks = manifold.recall(ticks + 1, default: 0)
```

Note that the output value of `manifold.recall` is used as an input value to
itself! This might seem strange in the sequential programming paradigm but it is
perfectly natural in hardware!

### Overloading

We said earlier that Manifold is single assignment but there is one exception to
this rule: assigning to a function variable multiple times will overload that
function to support different input and output types. For example

```
travel = manifold.Integer year -> manifold.Boolean success {
  success = travel (year, false)
}

travel = (year: manifold.Integer year, invisibility: manifold.Boolean invisibility) 
    -> manifold.Boolean success {
  ...
}
```

This example defines a function called `travel` which accepts either our time
machine input tuple or just an input year. Overloaded implementations may freely
call eachother.

## Types

 - `manifold.Boolean` is a single bit of data, with the value `manifold.high` or `manifold.false`. 
 - `manifold.Function` is an entity that produces an output value given an input value and potentially some internal state.
 - `manifold.Tuple` is a structured group of values.
 - `manifold.Enum`
 - `manifold.Integer`
 - `manifold.Type` is the "type" of all types in Manifold (including itself)

Since types are first class objects of type `manifold.Type`, a new type can be
defined via variable assignment. For example, the definition of Bit might look
like

```
Type Bit = manifold.Boolean
```

### Parameterized Types

Some types have compiletime parameters (like generics in C++). Such types are
defined via the `=>` syntax. Take, for example, this simple definition of an
`Array` type

```
Type Array = (manifold.Type T, manifold.Integer width) => (T...width, width: width)
```

Using this defined type, a developer could declare an instance of `Array` as

```
Array(SpaceshipEngine, 5) engines
```

## Constraints

*TODO*

## Packages and Namespacing

# Digital Hardware Core Library

 - `recall`
 - `count`
 - `cycle`

# Intermediate Language

 - **port**
 - **node**
 - **connection**
 - **constraint**

## Definition Files

### Port Definitions

### Node Definitions

### Connection Definitions

## Schematic Files

### Nodes

### Ports

### Connections

### Constraints

 







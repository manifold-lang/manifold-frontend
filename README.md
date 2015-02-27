# Introduction

In much the same way that computer programs are written in programming
languages, hardware is "written" in **hardware description languages**.

The two hardware languages in wide use today are **VHDL** and **Verilog**. Both
were designed in the early 1980's and both have remained frozen in time since,
while our understanding of how to build expressive, powerful, and usable
programming languages has grown considerably. These languages are in widespread
use because they are the best tool for the job of hardware design but they are
not nearly as expressive or approachable as modern programming languages.

More recently, several projects have spring up that provide tooling to use
existing *programming* languages as hardware languages. These projects include
**[CλaSH](http://clash.ewi.utwente.nl/ClaSH/Home.html)**,
**[MyHDL](http://www.myhdl.org/doku.php)**, and many others. This approach has
not gained widespread use because programming languages are not true to the
underlying domain of hardware programming (see [The Challenges of Hardware
Synthesis from C-like Languages](http://www1.cs.columbia.edu/~sedwardspapersedwards2005challenges.pdf)).

Manifold is a reimagining of a hardware design language that remains true to the underlying domain, like VHDL and Verilog, while also leveraging 30 years of
improved language design, like CλaSH and MyHDL. In particular, Manifold provides

 - affordances for creating reusable modules a package manager to enable
 - widespread distribution of modules a functional programming inspired language
 - that is true to the underlying domain a flexible compilation system, capable
 - of targeting not on;y digital hardware but other areas of system engineering
 - such as microfluidics a powerful constraint system capable of representing
 - complex requirements

# Philosophy

The compiler should work hard so that you don't have to. Prefer
implicit specification over explicit. Provide smart defaults and painless
overrides. Do as much work at compile time as possible. You should
express domain logic naturally and let the compiler decide how
to implement it. Use type inference wherever possible.

Optimize for readability over writeability. On average, developers spend 15
hours reading code and 5 hours modifying code for every 1 hour of writing code.
Avoid confusing abbreviations or acronyms. Enforce part-of-speech naming
conventions. Prefer longer, descriptive, unambiguous names (within reason). Code
should be as self-documenting as possible. Brevity is best for both readability
and writeability.

Be true to the underlying domain. Borrow concepts from sequential programming
where possible but avoid inaccurate polysonomy. Expose all hardware primitives
available as naturally as possible.

Build powerful self-hosted abstractions atop a minimal set of primitives.
Provide a powerful macro system to create the illusion of complex language
features. Build facades around complicated subsystems.

# Terminology, Notation and Conventions

## Documentation Conventions

 - Terminology with a specific technical definition will be **bold** the first time it is used to emphasize the precision and degree of specificity of the term.

## Glossary

*TODO*

 - backend
 - compiletime
 - connection
 - digital hardware
 - dynamic
 - frontend
 - node
 - port
 - runtime
 - schematic
 - static
 - tuple


## Naming Conventions

*There are only two hard problems in computer science: cache invalidation, naming things, and off by one errors*

Within the Manifold, there are strict conventions to make naming things easier and more consistent:

 - Avoid all acronyms and abbreviations that are not taught in introductory computer science courses, except where doing so becomes awkward or defies strong convention
 - Write type names in `UpperCamelCase` and all other names in `lowerCamelCase`
 - Always use the same word to refer to the same idea and different words to refer to different ideas
 - Prefer verb phrases for function names and noun phrases for all other names, except where doing so becomes awkward or defies strong convention 
 - Prefer one word names to two word names, two word names to three word names, etc. except where doing so creates ambiguity within a level of abstraction
 - Prefer shorter simpler words to longer more complicated words

# Frontend Language

The Manifold frontend language expresses systems in many problem
domains, including digital hardware and microfluidics, as text. It is optimized for conceptual elegance, expressiveness, and human readability.  

## Booleans

The most fundamental type in Manifold is the `manifold.Boolean` type. A
`manifold.Boolean` represents a single bit of information (true or false),
represented as `manifold.true` or `manifold.false`. For example, we might turn
on our time machine by setting

```
manifold.Boolean timeMachineOn = manifold.true
```

The compiler can generally infer the type of a variable so it is equivalent to
write

```
transmogrifier = manifold.true
```

## Annotations

Manifold supports a system capable of annotating variables with additional metadata using **annotations**. These annotations are similar to Java's annotations in syntax, being prefixed by a *@* and optionally taking parameters

```
@bar Integer width = 5
@foo(10, true) Integer height = 200
```

At the moment, annotations are defined by the compiler but user-defined annotations are planned for future versions of the spec.

## Static vs Dynamic Values

In Manifold, you should domain logic as naturally as possible and let the
compiler decide how to represent that logic in hardware.

To this end, almost any expression in Manifold can be evaluated either

 - on the sequential processor where the Manifold code is being compiled, at **compile time** statements are evaluated **statically**
 - on the physical hardware, at **run time** statements are evaluated **dynamically**

Certain operations, of course, can *only* be executed at a specific "time" --
for example, top level io ports may *only* be read dynamically at runtime.
Likewise, certain operations can *only* be executed statically at compiletime,
such as referencing an external file in the compilation environment.

Manifold is designed such that you do not need to think about the difference
between these two types of operations but take fine control over them, if
desired.

### Explicit Static vs Dynamic

Values may be constrained in whether they may be static or dynamic using annotations. For example, we can mandate that `width` is known statically (at compile time) by defining it as

```
@static Integer width 
```

## Tuples

A tuple is an ordered set of values that can be passed around as one logical
entity. Tuples are the glue that allow us to build domain objects -- like
numbers, genomes, and time machines -- out of `manifold.Boolean`s and other
primitive types.

A **tuple type** defines a format for a **tuple**, an ordered set of **named properties** and **unnamed properties**.

For example, suppose you are describing the input to some hardware for a time
machine that can travel to any year with an optional invisibility shield. You
could define a tuple which groups and names these variables

```
(year: manifold.Integer, invisibility: manifold.Boolean) input
```

and instantiate that tuple with some values as

```
input = (year: 5000, invisibility: manifold.true)
```

and access the properties in that instantiated tuple

```
input.invisibility # => 500
input.year         # => true
```

### Default Properties

The declaration may also include a default value for any property. 
```
(year: manifold.Integer = 5000, invisible: manifold.Boolean = manifold.false)
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

Arrays in Manifold are a special case of tuples which have many positional
"properties of a particular type. For example, an integer array of width 3 could
"be defined as

```
(manifold.Integer, manifold.Integer, manifold.Integer) array = (1, 2, 3)
```

or, with the equivalent shorthand,

```
(manifold.Integer...3) array = (1, 2, 3)
```

Sometimes it makes more sense to set the width of a tuple using a statically
evaluable expression. This often increases the readability and maintainability
of code.

```
manifold.Integer width = 5
(manifold.Integer...width) array = (1, 2, 3, 4, 5)
```

### Inferred Width Repeated Positional Properties

It is also possible to statically infer the width of a tuple statically from
the value being passed to it. Simply reference a named property within the same
tuple of type `manifold.Integer` instead of a width expression.

```
(manifold.Integer...width, width: manifold.Integer) array = (0, 1, 2, 3, 4, 5)
array.1      # => 1
array.width  # => 5
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

#### `*` Destructuring Assignment

The special symbol `*` on the lefthand side of an assignment operation allows you to extract all values from a tuple into local scope, called `*` destructuring assignment. For example, after the statement

```
* = (year: 5000, invisible: false)
```

We could reference `year` and `invisible` in the function's scope.

Note that the `*` operator does not act recursively or work on unnamed properties. If the tuple being destructures contains a property with the same name as a variable in local scope, a compiler error will be thrown.

Properites may be explicitly excluded from the destructuring assignment by including their name in parenthesis after the `*`

```
invisible = true
*(invisible) = (year: 5000, invisible: false)
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
there will be a compile time error

```
# Causes compile time TupleCastIllegalException
(manifold.Integer...3 = 0) array = (1, 2, 3, 4)
```

## Enums

An enum allows you to restrict a domain to a fixed set of named values. For example, if you want to represent the states of a traffic light, you might define a TrafficLightState enum as follows

```
Type TrafficLight = Enum(
    (green: boolean, yellow:boolean, red: boolean),
    green: (1, 0, 0),
    yellow: (0, 1, 0),
    red: (0, 0, 1),
    turn: (cycle(hz: 1), 0, 0)
)
```

This enum can then be used as 

```
TrafficLight south = TrafficLight.green
TrafficLight east = (0, 0, 1)
# But 'TrafficLight east = (0, 1, 1)' would be rejected by the compiler
```

If no type or values are specified for an enum, integers are used implicitly

```
Type Color = Enum(
    green,
    red,
    blue
)
Color color = Color.red
Color color = 0
```

## Functions

A function is an entity that, given an input value, uses some logic to produce
an output value.

Suppose you were to write a function that determined if an input to the time
machine was unsafe (i.e. targeting a year after the robot uprising and without the
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
machine input tuple or just an input year. Overloaded implementations may freely call eachother.

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

Some types have parameters (like generics in C++). Such types are
defined via the `=>` syntax. Take, for example, this simple definition of an
`Array` type

```
Type Array = (manifold.Type T, manifold.Integer width) => (T...width, width: width)
```

Using this defined type, a you could declare an instance of `Array` as

```
Array(SpaceshipEngine, 5) engines
```

Note that all parameters must be statically evaluable.

## Packages and Namespacing

Manifold features a modern package management system.

Values within packages (remember types and functions are just values) are brought into local scope using the `import` keyword and regular variable assignment. The following statement imports the `parallel` function from the `manifold` package.

```
parallel = import manifold.parallel
```

You may use destructuring assignment with packages

```
(parallel) = import manifold
```

and even `*` destructuring assignment to import everything from the package into local scope

```
* = import manifold
```

## Core Library

### parallel

### series

## Digital Hardware Library

### recall

### count

### cycle

# Intermediate Language

The Manifold intermediate language expresses systems in many problem
domains, including digital hardware and microfluidics, as JSON. It is optimized for simplicity and machine readability.  

The intermediate language describes systems in terms of three primitives:

 - **Nodes** are the entities of the system: latches, capacitors, reaction chambers, etc 
 - **Connections** relate nodes to eachother: wires, physical attachments, etc
 - **Ports** define what connections are allowed between nodes: a capacitor accepts one analog electircal input and one analog electrical output.

Each of these primitives are defined in terms of types (**node types**, **connection types**, and **port types**) and instances (**nodes**, **connections**, and **ports**). 

Type information goes into **definition files** which are shipped with the back end that supports them. For example, the digital hardware backend provides the digital hardware definition file which defines node types such as latches, clocks, and i/o pins.

Programs written in the Manifold frontend language compile into **schematic files** which can then be compiled into domain specific artifacts by the appropriate back end. For example, a description of a digital hardware circuit in the Manifold frontend would be compiled to a digital hardware schematic file that, when fed into the digitial harware backend, would produce a VHDL or Verilog file.

## Definition Files

```
{
  "node_types": {...}
  "connection_types": {...}
  "port_types": {...}
}
```

### Port Types

### Node Types

### Connection Types

## Schematic Files

```
{
  "nodes": {...}
  "connections": {...}
  "ports": {...}
}
```

### Nodes

### Ports

### Connections
 







Manifold is a high level language for creating all kinds of systems the way we
write software. Currently, it focuses on hardware and microfluidics design.

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
**[CλaSH](http://www.clash-lang.org/)**,
**[MyHDL](http://www.myhdl.org/doku.php)**, and many others. This approach has
not gained widespread use because programming languages are not true to the
underlying domain of hardware programming (see [The Challenges of Hardware
Synthesis from C-like Languages](http://www1.cs.columbia.edu/~sedwards/papers/edw
ards2005challenges.pdf)).

Manifold is a reimagining of a hardware design language that remains true to the
underlying domain, like VHDL and Verilog, while also leveraging 30 years of
improved language design, like CλaSH and MyHDL.

However, Manifold doesn't only do digital circuits -- it can be extended to
design all kinds of systems, including analog circuits, microfluidics, and
mechanical systems.

You should use Manifold because

 - it allows you to design circuits, microfluidics, and other systems in an elegant and consistent way
 - it allows you to express solutions to hard problems in simple text files
 - it allows you to encapsulate these solutions within "modules"
 - it allows you to reuse these modules within your own projects
 - it allows you to share modules among projects, teams, and organizations
 - it allows you to leverage the ecosystem of powerful software development tools

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

## Documentation Conventions

Terminology with a specific technical definition will be **bold** the first time it is used to emphasize the precision and degree of specificity of the term.

## Glossary

 - **back-end** refers to the process of converting the intermediate representation into a domain specific output product
 - **compile-time** refers to the schematic while it is being processed by the front-end and back-end compilers, as opposed run-time
 - **domain** refers to particular field of design, such as microfluidics or digital hardware; different domains have its different back-end compilers
 - **dynamic** refers to values that can be *only* determined at run-time
 - **front-end** refers to the high level language and the process of converting the high level language into a domain specific output product
 - **microfluidics** are miniaturized systems dealing with small volumes of fluids 
 - **run-time** refers to the schematic while it is being used in a domain-specific way after all Manifold compilation steps have completed, as opposed to compile-time
 - **schematic** refers to a description of a system in Manifold, analogous to a software program
 - **static** refers to values that can be determined at compile-time

## Naming Conventions

In order to make Manifold readable and conceptually consistent, the following naming conventions are adhered within Manifold and in the implementation of Manifold.

 - avoid all acronyms and abbreviations that are not taught in introductory computer science courses, except where doing so becomes awkward or defies strong convention 
 - write type names in `UpperCamelCase` and all other names in `lowerCamelCase`
 - always use the same word to refer to the same idea and different words to refer to different ideas (avoid confusing polysonomy and unnecessary synonymy)
 - Prefer verb phrases for function names and noun phrases for all other names, except where doing so becomes awkward or defies strong convention
 - Prefer one word names to two word names, two word names to three word names, etc

# Front-End Language

The Manifold front-end language expresses systems in many problem
domains, including digital hardware and microfluidics, as text. It is optimized for conceptual elegance, expressiveness, and human readability.  

## Booleans

The most fundamental type in Manifold is the `Boolean` type. A
`Boolean` represents a single bit of information: true or false,
represented as `true` or `false`. For example, we might turn
on our time machine by setting

```
Boolean timeMachineOn = true;
```

The compiler can infer the type of the variable so it is equivalent to write

```
timeMachineOn = true;
```

## Annotations

Manifold supports a system capable of annotating variables with additional metadata using **annotations**. These annotations are similar to Java's annotations in syntax, being prefixed by a *@* and optionally taking parameters

```
@bar Integer width = 5;
@foo(10, true) Integer height = 200;
```

At the moment, annotations are defined by the compiler but user-defined annotations are planned for future versions of the spec.

## Compile-Time vs Run-Time

In Manifold, you write domain logic as naturally as possible and let the
compiler decide how to represent that logic in hardware.

To this end, almost any expression in Manifold can be evaluated either

 - on the sequential processor where the Manifold code is being compiled, at **compile-time**
 - on the physical hardware, at **run-time**

Certain operations, of course, can *only* be executed at a specific "time" --
for example, top level I/O ports may *only* be read dynamically at run-time.
Likewise, certain operations can *only* be executed statically at compile-time,
such as referencing an external file in the compilation environment.

Manifold is designed so that you don't need to think about the difference
between these two types of operations but may take control over them, if
desired.

### Explicit Static vs Dynamic

Values may be explicitly constrained to being knowable at compile time or run time using annotations. For example, we can require that `width` is known statically (at compile time) by defining it as

```
@static Integer width;
```

If a variable or property is annotated as `@static` then we guarantee that its value is known at compile-time. Its value is additionally available at run-time.

If a variable or property is annotated as `@dynamic` then we guarantee that its value is *not* known at compile-time but is known at run-time.

## Tuples

A tuple is an ordered set of values that can be passed around as one logical
entity. Tuples are the glue that allow us to build domain objects -- like
numbers, genomes, and time machines -- out of `Boolean`s and other
primitive types.

For example, suppose you are describing the input to some hardware for a time
machine that can travel to any year with an optional invisibility shield. You
could define a tuple which groups and names these variables,

```
(year: Integer, invisibility: Boolean) input;
```

create such a tuple,

```
input = (year: 5000, invisibility: true);
```

and access the properties in that tuple

```
input.invisibility; // => 500
input.year;         // => true
```

### Default Properties

The declaration may also include a default value for any property. 

```
(year: Integer = 3000, invisible: Boolean = false);
```

Any property which does not have a default value is required; any property which
does have a default value is optional.

### Positional Properties

In addition to named properties, tuples may have implicitly named positional
properties; these properties are named by their position in the tuple. For
example, an ordered pair (x,y) might be represented as

```
(Integer, Integer);
```

where the first `Integer` is implicitly named `0` and the second `1`.

```
(Integer, Integer) position = (2, 4);
position.0;  // => 2
position.1;  // => 4
```

### Repeated Positional Properties (Arrays)

Arrays in Manifold are a special case of tuples which have many positional
properties of a particular type. For example, an integer array of width 3 could be defined as

```
(Integer, Integer, Integer) array = (1, 2, 3);
```

or, with the equivalent shorthand,

```
(Integer...3) array = (1, 2, 3);
```

Sometimes it makes more sense to set the width of a tuple using a compile-time
evaluable expression. This often increases the readability and maintainability
of code.

```
Integer width = 5;
(Integer...width) array = (1, 2, 3, 4, 5);
```

### Inferred Width Repeated Positional Properties

It is also possible to infer the width of a tuple statically from
the value being passed to it. Instead of a width expression, provide the definition of an `Integer` variable. This will create an additional property on the tuple containing the width of the array.

```
(Integer...Integer width) array = (0, 1, 2, 3, 4, 5);
array.1;     // => 1
array.width; // => 5
```

### Subscript Operator

*TODO This is super speculative. Do we even want to include this in the spec right now?*

```
Type TimeMachineSettings = (year: year, invisible: invisible);
TimeMachineSettings settings = (year: 5, invisible: high);

settings[TimeMachineSettings.Property.year];

TimeMachineSettings.Property property = TimeMachineSettings.Property.year;
settings[property];
```

### Destructuring Assignment

You may use destructuring assignment to extract the values from a tuple into
individual variables.

```
(year: Integer year, invisible: Boolean invisible) = (year: 5000, invisible: false);
```

This statement creates, in the local scope, the variables `year` and
`invisible`, assigned to `input.year` and `input.invisible` respectively.

Types in destructuring can be inferred by the compiler. It is functionally
equivalent to omit the types and write

```
(year: year, invisible: invisible) = (year: 5000, invisible: false);
```

Destructuring assignment need not extract all properties within a tuple. If a
you only need the destination year of your time machine, you need only write

```
(year: year) = (year: 5000, invisible: false);
```

### Casting

If a tuple is cast to another tuple type with additional properties and a
default value is provided for each of those properties, the cast will happen
successfully, using those default values.

```
(Integer...6 = 0) array = (1, 2, 3, 4);
// Array will have the value (1, 2, 3, 4, 0, 0)

(year: Integer = 5, invisibility: Boolean = false) settings = (year: 5);
// Settings will have the value (year: 5, invisibility: false)
```

If a tuple is cast to another tuple type with missing *named* properties, the
cast will happen successfully

```
(year: Integer) settings = (year: 5, invisibility: false);
// Settings will have the value (year: 5)
```

If a tuple is cast to another tuple type with missing *positional* properties,
there will be a compile-time error

```
// Causes compile-time TupleCastIllegalException
(Integer...3 = 0) array = (1, 2, 3, 4);
```

If a tuple with one property of type `A`, `(A)`, is cast to type `A`, then the value of the property will be extracted. In this way, parenthesis used to group statements work as expected.

```
Boolean winning = (true);
```

## Enums

An enum allows you to restrict a domain to a fixed set of named values. For example, if you want to represent the states of a traffic light, you might define a TrafficLightState enum as follows

```
Type TrafficLight = Enum(
    (green: Boolean, yellow: Boolean, red: Boolean),
    green: (1, 0, 0),
    yellow: (0, 1, 0),
    red: (0, 0, 1)
);
```

This enum can then be used as 

```
TrafficLight south = TrafficLight.green;
TrafficLight east = (0, 0, 1);
// But 'TrafficLight east = (0, 1, 1)' would be rejected by the compiler
```

If no type or values are specified for an enum, integers are used implicitly

```
Type Color = Enum(
    green,
    red,
    blue
);
Color color1 = Color.red;
Color color2 = 0;
```

## Functions

A function is an entity that, given an input value, uses some logic to produce
an output value.

Suppose you were to write a function that determined if an input to the time
machine was unsafe (i.e. targeting a year after the robot uprising and without the
invisibility shield). This function takes in some input in the form `(year:
Integer, invisible: Boolean invisible)` and produces some
output in the form of a `Boolean`.

```
TimeMachineInput = (year: Integer, invisible: Boolean invisible);

Function isDangerous = TimeMachineInput -> Boolean dangerous {
  dangerous = year > 5000 and !invisible;
};
```

Note in the above function that there is no `return` statement -- output
variables defined in the function definition are assigned to directly.

This function could be invoked as
```
settings = (year: 10000, invisibility: false);
dangerous = isDangerous settings;
```

Multiple functions may be chained together as
```
if (!isDangerous settings) {
  travel startFluxCapacitor configureTimeCrystals settings;
}
```

where the returned value of `configureTimeCrystals` is passed to
`startFluxCapacitor` and that of `startFluxCapacitor` is passed to `travel`.

### Stateful Functions

Functions in Manifold are not "pure" -- they may have state. For example, the
count function supplied by the core library can be used to track system uptime
in seconds as

```
Integer uptime = count(clock(hz: 1));
```
This function can "remember" values from clock tick to clock tick 

All state in Manifold is derived from the `recall` primitive function
```
Function recall = (T next, default: T) -> T current {...};
```

This primitive acts like a hardware flipflop in that the output value `current`
will always take on the value that the input `next` had during the previous
clock tick. As an example, we could implement a simple counter as

```
Integer ticks = recall(ticks + 1, default: 0);
```

Note that the output value of `recall` is used as an input value to
itself! This might seem strange in the sequential programming paradigm but it is
perfectly natural in hardware!

### Overloading

We said earlier that Manifold is single assignment but there is one exception to
this rule: assigning to a function variable multiple times will overload that
function to support different input and output types. For example

```
travel = Integer year -> Boolean success {
  success = travel (year, false);
};

travel = (year: Integer year, invisibility: Boolean invisibility) 
    -> Boolean success {
  ...
};
```

This example defines a function called `travel` which accepts either our time
machine input tuple or just an input year. Overloaded implementations may freely call each other.

## Types

 - `Boolean` is a single bit of data, with the value `high` or `false`. 
 - `Function` is an entity that produces an output value given an input value and potentially some internal state.
 - `Tuple` is a structured group of values.
 - `Enum`
 - `Integer`
 - `Type` is the "type" of all types in Manifold (including itself)

Since types are first class objects of type `Type`, a new type can be
defined via variable assignment. For example, the definition of Bit might look
like

```
Type Bit = Boolean;
```

### Parameterized Types

Some types have compile-time parameters (like generics in C++). Such types are
defined via the `=>` syntax. Take, for example, this simple definition of an
`Array` type

```
Type Array = (Type T, Integer width) => (T...width, width: width);
```

Using this defined type, a developer could declare an instance of `Array` as

```
Array(SpaceshipEngine, 5) engines;
```

## Constraints

*TODO*

## Packages and Namespacing

# Digital Hardware Core Library

 - `recall`
 - `count`
 - `cycle`

```
parallel = import parallel;
```

You may use destructuring assignment with packages

```
(parallel) = import Manifold;
```

or `*` destructuring assignment to import everything from the package into local scope

```
* = import Manifold;
```

## Core Library

### `Boolean`

### `Integer`

### `Tuple`

### `Void`

A type describing the lack of a value. Equivalent to `()`.

### `Type`

### `parallel <T, U> ((T -> U)...@static Integer width) -> (T...width)`

### `series <T> ((T -> T)...@static Integer width) -> T`

## Digital Hardware Library

### `recall<T> (T initial, T next) -> T`

### `count (Integer hz, Integer mod) -> Integer`

### `cycle<T> (Integer hz, T...) -> T`

### `if ((Boolean, Void -> Void)...)`

## Microfluidics Library

# Intermediate Language

 - **port**
 - **node**
 - **connection**
 - **constraint**

The intermediate language describes systems in terms of three primitives:

 - **Nodes** are the entities of the system such as latches, capacitors, reaction chambers, etc 
 - **Connections** relate nodes to each other, such as with wires, physical attachments, etc
 - **Ports** define what connections are allowed between nodes, for example a capacitor accepts one analog electrical input and one analog electrical output.

Each of these primitives are defined in terms of types (**node types**, **connection types**, and **port types**) and instances (**nodes**, **connections**, and **ports**). 

Type information goes into **definition files** which are shipped with the back end that supports them. For example, the digital hardware back-end provides the digital hardware definition file which defines node types such as latches, clocks, and i/o pins.

Programs written in the Manifold front-end language compile into **schematic files** which can then be compiled into domain specific artifacts by the appropriate back end. For example, a description of a digital hardware circuit in the Manifold front-end would be compiled to a digital hardware schematic file that, when fed into the digital hardware back-end, would produce a VHDL or Verilog file.

## Definition Files

Each back-end compiler includes a definition file which declares all of its supported primitives. 

```
{
  "node_types": {...}
  "connection_types": {...}
  "port_types": {...}
}
```

### Node Definitions

```
{
  "port_types": {
    "name": {
      attributes: {...}
    }
    , ...
  }
}
```

### Node Types

```
{
  "node_types": {
    "name": {
      ports: ["name"]
      attributes: {...}
    }
    , ...
  }
}
```

### Connection Types

## Schematic Files

A schematic written in the Manifold front-end language will compile into a  intermediate file of this format. 

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

### Constraints

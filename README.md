Manifold is a high level language for creating all kinds of systems the way we
write software. Currently, it focuses on hardware and microfluidics design.

# Introduction

In much the same way that computer programs are written in programming
languages, digital circuits is "written" in a **hardware description language**.

The two hardware languages in wide use today are **VHDL** and **Verilog**. Both
were designed in the early 1980s and both have remained frozen in time since,
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

## Bools

The most fundamental type in Manifold is the `Bool` type. A
`Bool` represents a single bit of information: true or false,
represented as `true` or `false`. For example, we might turn
on our time machine by setting

```
timeMachineOn = true;
```

## Ints

The `Int` data type is used to represent positive integers.

```
year = 5000;
```

## Tuples

A tuple is an ordered set of values that can be passed around as one logical
entity. Tuples are the glue that allow us to build domain objects -- like
numbers, genomes, and time machines -- out of `Bool`s and other
primitive types.

For example, suppose you are describing the input to some hardware for a time
machine that can travel to any year with an optional invisibility shield. You
could define a tuple which groups and names these variables,

```
input = (year=5000, invisibility=true);
```

and access the properties in that tuple,

```
input.invisibility; // => 5000
input.year;         // => true
```

Variables in a tuple can also be unnamed. Unnamed properties are identified by their position and accessed through an integer index.

```
input = (1, 2, 3);

input[0]; // => 1
input[1]; // => 2
```

### Destructuring Assignment

You may use destructuring assignment to extract the values from a tuple into
individual variables.

```
(year, invisible) = (year: 5000, invisible: false);
```

This statement creates, in the local scope, the variables `year` and
`invisible`, assigned to the `year` and `invisible` properties of the tuple, respectively.

Destructuring assignment need not extract all properties within a tuple. If a
you only need the destination year of your time machine, you need only write

```
(year=year) = (year=5000, invisible=false);
```

## Typing System

 - `Type` is the "type" of all types in Manifold (including itself)
 - `Bool` is a single bit of data, with the value `true` or `false`.
 - `Function` is an entity that produces an output value given an input value and potentially some internal state.
 - `Tuple` is a structured group of values.
 - `Enum`
 - `Int`

Since types are first class objects of type `Type`, a new type can be
defined via variable assignment. For example, the definition of Bit might look
like

```
Type Bit = Bool;
```

Types are compared structurally, so as long as two tuples have the same field
names, they are assignable. In this example the right hand side is an anonymous
tuple type, but it is assigned to the named tuple type `Tuple1`.

```
Type Tuple1 = (a: Int, b: Int);
Tuple1 a = (a=1, b=2);
```

## Functions

A function is an entity that, given an input value, uses some logic to produce
an output value.

Suppose you were to write a function that determined if an input to the time
machine was unsafe (i.e. targeting a year after the robot uprising and without the
invisibility shield). This function takes in some input in the form `(year:
Int, invisible: Bool invisible)` and produces some
output in the form of a `Bool`.

```
reversedTuple = (a: Bool, b: Bool) -> (c: Bool, d: Bool) {
  c = b;
  d = a;
};
```

Note in the above function that there is no `return` statement -- output
variables defined in the function definition are assigned to directly.

This function could be invoked as,

```
(foo, bar) = reversedTuple(a = true, b = false);
// (foo = false, bar = true)
```

## Package System

A package and import system makes it possible to combine multiple Manifold files together into one schematic. Imported files contribute their declarations and definitions into the importing file's scope.

```
lib = import "microfluidics";
p = lib.fluidEntry();
```

A module can export values using the `public` keyword. All other values are private to the module

```
public microfluidPort = primitive port Bool;
public fluidEntry = primitive node (Nil) -> (out: microfluidPort)

// Private variable
x = fluidEntry();
```

## Comments

Manifold supports C++-style comment syntax, with both single-line `//` comments and multiline `/* ... */` comments.

```
/*
This is a comment
*/

// This is also a comment.
```

# Planned or Speculative Features

The features described in this section are not currently part of the Manifold language. They may be added in future versions depending on need.

## Enums

An enum allows you to restrict a domain to a fixed set of named values. For example, if you want to represent the states of a traffic light, you might define a TrafficLightState enum as follows

```
Type TrafficLight = Enum(
    (green: Bool, yellow: Bool, red: Bool),
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

## Arrays

Arrays in Manifold are a special case of tuples which have many positional
properties of a particular type. For example, an integer array of width 3 could be defined as

```
(Int, Int, Int) array = (1, 2, 3);
```

or, with the equivalent shorthand,

```
(Int...3) array = (1, 2, 3);
```

Sometimes it makes more sense to set the width of a tuple using a compile-time
evaluable expression. This often increases the readability and maintainability
of code.

```
Int width = 5;
(Int...width) array = (1, 2, 3, 4, 5);
```

### Inferred Width Repeated Positional Properties

It is also possible to infer the width of a tuple statically from
the value being passed to it. Instead of a width expression, provide the definition of an `Int` variable. This will create an additional property on the tuple containing the width of the array.

```
(Int...Int width) array = (0, 1, 2, 3, 4, 5);
array.1;     // => 1
array.width; // => 5
```

### Casting

If a tuple is cast to another tuple type with additional properties and a
default value is provided for each of those properties, the cast will happen
successfully, using those default values.

```
(Int...6 = 0) array = (1, 2, 3, 4);
// Array will have the value (1, 2, 3, 4, 0, 0)

(year: Int = 5, invisibility: Bool = false) settings = (year: 5);
// Settings will have the value (year: 5, invisibility: false)
```

If a tuple is cast to another tuple type with missing *named* properties, the
cast will happen successfully

```
(year: Int) settings = (year: 5, invisibility: false);
// Settings will have the value (year: 5)
```

If a tuple is cast to another tuple type with missing *positional* properties,
there will be a compile-time error

```
// Causes compile-time TupleCastIllegalException
(Int...3 = 0) array = (1, 2, 3, 4);
```

If a tuple with one property of type `A`, `(A)`, is cast to type `A`, then the value of the property will be extracted. In this way, parenthesis used to group statements work as expected.

```
Bool winning = (true);
```

### Parameterized Types

Some types have compile-time parameters (like generics in C++). Such types are
defined via the `=>` syntax. Take, for example, this simple definition of an
`Array` type

```
Type Array = (Type T, Int width) => (T...width, width: width);
```

Using this defined type, a developer could declare an instance of `Array` as

```
Array(SpaceshipEngine, 5) engines;
```

## Function Overloading

Manifold is single assignment but there is one exception to
this rule: assigning to a function variable multiple times will overload that
function to support different input and output types. For example

```
travel = (year: Int) -> (success: Bool) {
  success = travel (year, false);
};

travel = (year: Int, invisibility: Bool) -> (success: Bool) {
  // ...
};
```

This example defines a function called `travel` which accepts either our time
machine input tuple or just an input year. Overloaded implementations may freely call each other.

## Digital Hardware Core Library

 - `series <T> ((T -> T)...@static Int width) -> T`
 - `recall<T> (T initial, T next) -> T`
 - `count (Int hz, Int mod) -> Int`
 - `cycle<T> (Int hz, T...) -> T`
 - `if ((Bool, Void -> Void)...)`

### Stateful Functions

Functions in Manifold are not "pure" -- they may have state. For example, the
count function supplied by the core library can be used to track system uptime
in seconds as

```
Int uptime = count(clock(hz: 1));
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
Int ticks = recall(ticks + 1, default: 0);
```

Note that the output value of `recall` is used as an input value to
itself! This might seem strange in the sequential programming paradigm but it is
perfectly natural in hardware!

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

## Schematic Files

A schematic written in the Manifold front-end language will compile into a  intermediate file of this format. 

```
{
  "name": "..."
  "userDefinedTypes": {...}
  "portTypes": {...}
  "nodeTypes": {...}
  "constraintTypes": {...}
  "nodes": {...}
  "connections": {...}
  "constraints": {...}
}
```
### Port Types

```
"portTypes": {
  "name": {
    "signalType": "...",
    "attributes": {...}
  },
  ...
},
```

### Node Types


```
"nodeTypes": {
  "name": {
    "attributes": {...},
    "ports": {...}
  },
  ...
},
```

### Nodes

```
"nodes": {
  "name": {
    "type": "...",
    "attributes": {...},
    "portAttrs": {...}
  },
  ...
},
```

### Connections

```
"connections": {
  "name": {
    "attributes": {...},
    "from": "...",
    "to": "..."
  },
  ...
},
```



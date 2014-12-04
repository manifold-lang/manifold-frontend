package org.manifold.compiler.front;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

public class TypeChecker implements ExpressionVisitor {

  private static Logger log = LogManager.getLogger("TypeChecker");

  // type of last visited expression
  private TypeValue type;

  private Map<NamespaceIdentifier, Namespace> namespaces;
  private Namespace environment;
  private Scope currentScope;

  public TypeChecker(Map<NamespaceIdentifier, Namespace> namespaces,
      Namespace env) {
    this.namespaces = namespaces;
    this.environment = env;
  }

  public void check() throws VariableNotAssignedException {
    currentScope = environment.getPrivateScope();
    for (VariableIdentifier vi : currentScope.getSymbolIdentifiers()) {
      try {
        Variable v = currentScope.getVariable(vi);
        analyze(v);
      } catch (VariableNotDefinedException e) {
        throw new UndefinedBehaviourError(
            "inconsistency: variable identifier '" + vi.toString() +
            " present in scope but not actually defined");
      }
    }
  }

  public static void typecheck(
      Map<NamespaceIdentifier, Namespace> namespaces,
      Namespace env) throws VariableNotAssignedException {
    TypeChecker t = new TypeChecker(namespaces, env);
    t.check();
  }

  /**
   * Analyze the type of a variable and assign that type.
   * Return the type that was assigned.
   * @throws VariableNotAssignedException
   */
  private TypeValue analyze(Variable v) throws VariableNotAssignedException {
    if (v.getType().equals(UnknownTypeValue.getInstance())) {
      log.debug("analyzing type of variable '" + v.getIdentifier() + "'");
      Expression rhs = v.getValueExpression();
      rhs.accept(this);
      v.setType(type);
      return type;
    } else {
      log.debug("type of variable '" + v.getIdentifier() + "' already known");
      return v.getType();
    }
  }

  @Override
  public void visit(FunctionInvocationExpression functionInvocationExpression) {
    log.debug("analyzing type of function invocation expression '"
        + functionInvocationExpression.toString() + "'");

    functionInvocationExpression.getFunctionExpression().accept(this);
    TypeValue functionType = this.type;
    log.debug("function has type " + functionType.toString());

    functionInvocationExpression.getInputExpression().accept(this);
    TypeValue argumentType = this.type;
    log.debug("argument has type " + argumentType.toString());

    TypeValue resultType = new TypeVariable();
    makeTypesEqual(new FunctionTypeValue(argumentType, resultType),
        functionType);
    this.type = prune(resultType);
    log.debug("result has type " + this.type.toString());
  }

  @Override
  public void visit(LiteralExpression literalExpression) {
    // this one is easy because all values know their type
    Value v = literalExpression.getValue(currentScope);
    this.type = v.getType();
    log.debug("type of literal expression '" + v.toString()
        + "' is " + this.type.toString());
  }

  @Override
  public void visit(VariableAssignmentExpression variableAssignmentExpression) {
    throw new UnsupportedOperationException("type-checking variable assignment"
        + " is currently unimplemented");
  }

  @Override
  public void visit(VariableReferenceExpression variableReferenceExpression) {
    VariableIdentifier vid = variableReferenceExpression.getIdentifier();
    NamespaceIdentifier id = vid.getNamespaceIdentifier();
    Namespace ns = checkNotNull(namespaces.get(id));
    try {
      Variable v = ns.getPrivateScope().getVariable(vid);
      type = analyze(v);
    } catch (VariableNotDefinedException e) {
      throw new TypeError(e);
    } catch (VariableNotAssignedException e) {
      throw new TypeError(e);
    }
  }

  @Override
  public void visit(
      PrimitivePortDefinitionExpression portDefExpr) {
    log.debug("analyzing type of primitive port definition expression '"
        + portDefExpr.toString() + "'");
    // two things to check:
    // 1. the port signal expression "names a type", i.e. it is a TypeTypeValue
    // TODO
    // 2. every attribute "names a type"
    // TODO

    this.type = TypeTypeValue.getInstance();
  }

  class TypeError extends Error {
    private static final long serialVersionUID = -6551145239250515985L;
    private final Throwable cause;
    @Override
    public Throwable getCause() {
      return cause;
    }
    public TypeError(Throwable cause) {
      this.cause = cause;
    }
    @Override
    public String getMessage() {
      return cause.getMessage();
    }
  }

  // Create assignments between corresponding type variables and concrete types
  // in order to make the two given type values equal
  // (if this is possible).
  // Most implementations of the Hindley-Milner type system call this "unify()".
  public void makeTypesEqual(TypeValue t1, TypeValue t2) {
    TypeValue a = prune(t1);
    TypeValue b = prune(t2);
    log.debug("unifying types " + a.toString() + " and " + b.toString());
    if (a instanceof TypeVariable) {
      if (!a.equals(b)) {
        if (occursInType((TypeVariable) a, b)) {
          throw new UndefinedBehaviourError("recursive unification of types "
              + "'" + a.toString() + "' and '" + b.toString() + "'");
        } else {
          ((TypeVariable) a).setInstance(b);
        }
      }
    } else if (a instanceof FunctionTypeValue) {
      if (b instanceof TypeVariable) {
        makeTypesEqual(b, a);
      } else if (b instanceof FunctionTypeValue) {
        FunctionTypeValue fa = (FunctionTypeValue) a;
        FunctionTypeValue fb = (FunctionTypeValue) b;
        makeTypesEqual(fa.getInputType(), fb.getInputType());
        makeTypesEqual(fa.getOutputType(), fb.getOutputType());
      } else {
        throw new UndefinedBehaviourError("cannot unify types "
            + "'" + a.toString() + "' and '" + b.toString() + "'");
      }
    } else {
      // a is neither a type variable nor a type operator;
      // therefore a is a concrete type
      if (b instanceof TypeVariable) {
        makeTypesEqual(b, a);
      } else if (b instanceof FunctionTypeValue) {
        throw new UnsupportedOperationException("not implemented");
      } else {
        // now b is also a concrete type
        // just check that these two types are equivalent
        if (!(a.equals(b))) {
          throw new UndefinedBehaviourError("cannot unify types "
              + "'" + a.toString() + "' and '" + b.toString() + "'");
        }
      }
    }
  }

  public TypeValue prune(TypeValue t) {
    if (t instanceof TypeVariable) {
      TypeVariable tv = (TypeVariable) t;
      if (tv.getInstance() != null) {
        tv.setInstance(prune(tv.getInstance()));
        return tv.getInstance();
      }
    }
    return t;
  }

  public boolean occursInType(TypeVariable v, TypeValue type2) {
    TypeValue pruned2 = prune(type2);
    if (v.equals(type2)) {
      return true;
    } else if (pruned2 instanceof FunctionTypeValue) {
      FunctionTypeValue f = (FunctionTypeValue) pruned2;
      return occursInType(v, f.getInputType())
          || occursInType(v, f.getOutputType());
    } else {
      return false;
    }
  }

  @Override
  public void visit(
      PrimitiveNodeDefinitionExpression primitiveNodeDefinitionExpression) {
    // TODO(murphy)
    this.type = TypeTypeValue.getInstance();
  }

  @Override
  public void visit(TupleTypeValueExpression tupleTypeValueExpression) {
    // TODO(murphy)
    this.type = TypeTypeValue.getInstance();
  }

  @Override
  public void visit(FunctionTypeValueExpression functionTypeValueExpression) {
    // TODO(murphy)
    this.type = TypeTypeValue.getInstance();
  }

}

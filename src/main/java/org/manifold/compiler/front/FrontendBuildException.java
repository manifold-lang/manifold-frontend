package org.manifold.compiler.front;

/**
 * Use this for errors that should be reported directly to the user.
 * Non-internal errors should be translated to a FrontendBuildException somewhere in the pipeline.
 */
public class FrontendBuildException extends Error {
  FrontendBuildException(String msg) {
    super(msg);
  }
}


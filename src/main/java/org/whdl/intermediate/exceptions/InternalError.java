package org.whdl.intermediate.exceptions;

public class InternalError extends Error {

  /**
   * 
   */
  private static final long serialVersionUID = 4302832267210981600L;

  private String msg;
  
  public InternalError(String msg){
    this.msg = msg;
  }
  
  @Override
  public String getMessage(){
    return "internal error: " + msg;
  }
  
}

package it.unisa.justTraditions.storage.util;

import java.util.List;

/**
 * Questa classe implementa la funzionalità di validare la chiamata a una funzione.
 */
public abstract class OnlyStorageCall {
  private static final StackWalker stackWalker = StackWalker.getInstance();

  /**
   * Implementa la funzionalità di convalida della chiamata.
   * IllegalCallerException se il metodo viene chiamato al di fuori del package Storage.
   */
  public static void validateCall() {
    List<StackWalker.StackFrame> stackFrames = stackWalker.walk(s ->
        s.skip(1)
            .limit(2)
            .toList()
    );

    String callerClassName = stackFrames.get(1).getClassName();
    if (!callerClassName.startsWith("it.unisa.justTraditions.storage")) {
      String methodName = stackFrames.get(0).getMethodName();
      throw new IllegalCallerException(methodName + " called from " + callerClassName);
    }
  }
}

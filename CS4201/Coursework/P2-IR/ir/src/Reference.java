
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

class Funs {
    Class<?> c = Reference.class;
    String fn;
    int fn_args;
    Object[] args;

    Funs(String fn, int fn_args, Object[] args) {
        this.fn = fn;
        this.fn_args = fn_args;
        this.args = args;
    }
}

class Con {
    String type;
    Object x;
    Con xs;
    public Con(String type, Object x, Con xs) {
        this.type = type;
        this.x = x;
        this.xs = xs;
    }
}

public class Reference {

    public static Object apply(Funs fun, Object[] args) {

        // Apply the new args to the fun parameter list
        fun.args = Stream.concat(Arrays.stream(fun.args), 
            Arrays.stream(args)).toArray();
        
        try {
    
            // Fetch the method
            Method method = fun.c.getDeclaredMethod(fun.fn, Object[].class);
    
            // Under-application - return the updated Funs object
            if (fun.args.length < fun.fn_args) {
                return fun;
    
            // Over-application - eval function and apply extra arguments to result
            } else if (fun.args.length > fun.fn_args) {
                Object[] extra_args = Arrays.copyOfRange(fun.args, 
                    fun.fn_args, fun.args.length);
                fun.args = Arrays.copyOfRange(fun.args,0,fun.fn_args);
                return apply((Funs) method.invoke(null, 
                    Arrays.copyOfRange(fun.args,0,fun.fn_args)), 
                        extra_args);
    
            // Fully-applied - return the evaluation result
            } else {
                return method.invoke(null, (Object) fun.args);
            }
            
        } catch (Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

public static Object factorial2(Object... a){
Object x = a[0];
Object test = a[1];
Object ANFF_il = x;
Object ANFF_ir = 0;
Object ANFF_i = (ANFF_il==ANFF_ir) ? 1 : 0;
if ((int) ANFF_i != 0) {
return 1;
} else {
Object ANFF_iel = x;
Object ANFF_ierfal = x;
Object ANFF_ierfar = 1;

Object ANFF_ierfa = (int) ANFF_ierfal - (int) ANFF_ierfar;
Object ANFF_ier = factorial2(ANFF_ierfa, "");
return (int) ANFF_iel * (int) ANFF_ier;
}

}

public static Object testFunction() {
    Object ANFMfal = 3;
    Object ANFMfar = 9;
    Object ANFMfa = (int) ANFMfal + (int) ANFMfar;
    Object[] args = { ANFMfa, ANFMfa };
    return apply(new Funs("factorial2", 2, new Object[0]), args);
}

public static Object testCon() {
    Object ANFCa = 3;
    Object ANFCaa = "T";
    Con ANFC = new Con ("Con", ANFCa, new Con("Con", ANFCaa, new Con("Nil", null, null)));
    switch (ANFC.type) {
        case "Con":
            Object x = ANFC.x;
            Object xs = ANFC.xs;
            return x;
        case "Nil":
            return 0;
    }
    return null;
}

public static Object main_function(){
return testFunction();
}
public static void main(String[] args){
    System.out.println(main_function());
} 
}


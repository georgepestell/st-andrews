import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;
class Fun { 
   Class<?> c = Test.class; 
   String fn; 
   int fn_args; 
   Object[] args; 

   Fun(String fn, int fn_args, Object[] args) { 
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

public class Test {
public static Object apply(Fun fun, Object[] args) {
   Object[] new_args = Stream.concat(Arrays.stream(fun.args), Arrays.stream(args))
                       .toArray();
   fun.args = new_args;
   Method method;
   try {
     method = fun.c.getDeclaredMethod(fun.fn, Object[].class);
   } catch (Exception e ) {
     e.printStackTrace();
     return null;
   }

   if (fun.args.length < fun.fn_args) {
     return fun;
   } else if (fun.args.length > fun.fn_args) {
     try { 
       Object[] extra_args = Arrays.copyOfRange(fun.args, fun.fn_args, fun.args.length);
       fun.args = Arrays.copyOfRange(fun.args, 0, fun.fn_args);
       return apply((Fun) method.invoke(null, (Object) Arrays.copyOfRange(fun.args, 0, fun.fn_args)), extra_args);
     } catch (Exception e) { 
       System.err.println("Apply error: Function called with invalid arguments");
       return null;
     }
   } else {
     try {
       return method.invoke(null, (Object) fun.args);
     } catch (Exception e) {
       System.err.println(e.getMessage());
       e.printStackTrace();
       return null;
     }
   }
}
public static Object double2(Object... args){
Object val = args[0];

Object ANFF_l = val;
Object ANFF_r = 2;
return (int) ANFF_l * (int) ANFF_r;

}
public static Object factorial2(Object... args){
Object x = args[0];

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
Object ANFF_ier = factorial2(ANFF_ierfa);
return (int) ANFF_iel * (int) ANFF_ier;
}

}
public static Object fst(Object... args){
Object p = args[0];

Object ANFF_C = p;
switch(((Con) ANFF_C).type) {
case "MkPair":
  Object x = ((Con) ANFF_C).x;
  Object y = ((Con) ANFF_C).xs;
return x;
default:
  return null;
}

}
public static Object snd(Object... args){
Object p = args[0];

Object ANFF_C = p;
switch(((Con) ANFF_C).type) {
case "MkPair":
  Object x = ((Con) ANFF_C).x;
  Object y = ((Con) ANFF_C).xs;
return y;
default:
  return null;
}

}
public static Object sum(Object... args){
Object xs = args[0];

Object ANFF_C = xs;
switch(((Con) ANFF_C).type) {
case "Nil":
return 0;
case "Cons":
  Object y = ((Con) ANFF_C).x;
  Object ys = ((Con) ANFF_C).xs;
Object ANFF_Cssl = y;
Object ANFF_Cssrfa = ys;
Object ANFF_Cssr = sum(ANFF_Cssrfa);
return (int) ANFF_Cssl + (int) ANFF_Cssr;
default:
  return null;
}

}
public static Object testlist(Object... args){

Object ANFF_ca = 1;
Object ANFF_caa = 2;
return new Con("Cons",ANFF_ca, new Con("Cons",ANFF_caa, new Con ( "Nil", null, null)));

}
public static Object map(Object... args){
Object f = args[0];
Object xs = args[1];

Object ANFF_C = xs;
switch(((Con) ANFF_C).type) {
case "Nil":
return new Con ( "Nil", null, null);
case "Cons":
  Object y = ((Con) ANFF_C).x;
  Object ys = ((Con) ANFF_C).xs;
Object ANFF_Csscafa = y;
Object ANFF_Cssca = apply ((Fun) f, new Object[]{ANFF_Csscafa});
Object ANFF_Csscaafa = f;
Object ANFF_Csscaafaa = ys;
Object ANFF_Csscaa = map(ANFF_Csscaafa,ANFF_Csscaafaa);
return new Con("Cons",ANFF_Cssca, new Con("Cons",ANFF_Csscaa, new Con ( "Nil", null, null)));
default:
  return null;
}

}
public static Object mainFunction(){
Object ANFMA = new Fun("fst", 1, new Object[]{});
Object ANFMAaca = 1;
Object ANFMAacaa = 2;
Con ANFMAa = new Con("MkPair",ANFMAaca, new Con("MkPair",ANFMAacaa, new Con ( "Nil", null, null)));
return apply((Fun) ANFMA, new Object[]{ANFMAa});
}
public static void main(String[] args){
System.out.println(mainFunction());
} 
}


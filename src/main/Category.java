package main;

@FunctionalInterface 
public interface Category {
     void fetch();
    
     
     default String getName() {
    	 return "NAme";
     }
}

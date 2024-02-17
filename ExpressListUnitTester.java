import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExpressListUnitTester {

	@Test
	public void constructorInterfaceTester_10_pts() {
		Field[] fields = ExpressLinkedList.class.getDeclaredFields();
		for (Field f: fields){
			assertTrue("List class contains a public field", 
					!Modifier.isPublic(f.getModifiers()));
		}

		assertTrue ("Number of constructors != 1", 
				ExpressLinkedList.class.getDeclaredConstructors().length == 1);

		assertTrue ("List interface not implemented or other interfaces are", 
				ExpressLinkedList.class.getInterfaces().length == 1 
				&& ExpressLinkedList.class.getInterfaces()[0].getName().equals("java.util.List"));
	}

	@Test
	public void addTester1_5_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		//check for exceptions
		try{
			list.add(1, "0");
			fail("Exception was to be thrown");
		}catch (IndexOutOfBoundsException e){
			//OK
		}catch (Exception ex) {
			fail("Wrong type of exception");
		}

		list.add("0");
		assertTrue(list.get(0).equals("0"));
	}

	@Test
	public void addTester2_10_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		list.add(0, "0");
		list.add(0, "1");
		list.add(1, "2");
		assertTrue(list.toString().equals("[1, 2, 0]") 
				|| list.get(0).equals("1") && list.get(1).equals("2") && list.get(2).equals("0"));
	}

	@Test
	public void removeTester1_5_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		//check for exception
		try{
			list.remove(0);
			fail("Exception was to be thrown");
		}catch (IndexOutOfBoundsException e){
			//OK
		}catch (Exception ex) {
			//fail("Wrong type of exception");
		}
	}

	@Test
	public void removeTester2_10_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		list.add("1"); list.add("2"); list.add("3");
		assertTrue(list.size() == 3);
		list.remove(1);
		assertTrue(list.size() == 2);
		assertTrue(list.toString().equals("[1, 3]") 
				|| list.get(0).equals("1") && list.get(1).equals("3"));
	}

	@Test
	public void randomAddRemove_25_pts(){
		Random rnd = new Random();
		List <Integer> list1 = new ArrayList<>();
		List <Integer> list2 = new ExpressLinkedList<>();
		for (int i = 0; i < 30000; i++){
			if (rnd.nextDouble() < 0.5){
				int position = rnd.nextInt(list1.size()+1);
				//int value = rnd.nextInt(1000);
//				list1.add(position, i);
//				list2.add(position, i);
			}
			else{
				if (list1.size() > 0){
					int position = rnd.nextInt(list1.size());
					list1.remove(position);
					//System.out.println("L1: "+list1);
					list2.remove(position);
					//System.out.println("L2: "+list2);
				}
			}
		}

		//assertTrue("randomAddRemove_25_pts: toString" , list1.toString().equals(list2.toString()));
		for (int i = 0; i < list1.size(); i++){
			assertTrue("randomAddRemove_25_pts: " + list2.size() + ": " + i + "\n"+ list1 + "\n" + list2, 
						list1.get(i).equals(list2.get(i)));
		}
	}

	@Test
	public void getTester1_5_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		//check for exceptions
		try{
			list.get(0);
			fail("Exception was to be thrown");
		}catch (IndexOutOfBoundsException e){
			//OK
		}catch (Exception ex) {
			//fail("Wrong type of exception");
		}
	}

	@Test
	public void getTester2_5_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		list.add("0");list.add("1");
		assertTrue(list.get(0).equals("0"));
		assertTrue(list.get(1).equals("1"));
	}

	@Test
	public void clearTester_5_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		assertTrue(list.size() == 0);

		list.add("1"); list.add("2"); list.add("3");
		//assertTrue(!list.isEmpty());
		assertTrue(list.size() == 3);

		list.clear();
		assertTrue(list.size() == 0);

		try{
			list.get(0);
			fail("Exception was to be thrown");
		}catch (IndexOutOfBoundsException e){
			//OK
		}catch (Exception ex) {
			//fail("Wrong type of exception");
		}
	}

	@Test
	public void sizeTester_5_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		assertTrue(list.size() == 0);
		list.add("1");
		assertTrue(list.size() == 1);
		list.add("9");
		assertTrue(list.size() == 2);
	}

	@Test
	public void toStringTester1_5_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		assertTrue(list.toString().equals("[]"));
	}

	@Test
	public void toStringTester2_10_pts() {
		ExpressLinkedList<String> list = new ExpressLinkedList<>();
		list.add("1"); list.add("2"); list.add("3");
		assertTrue(list.toString().equals("[1, 2, 3]"));
	}
	
	@Test (timeout = 2000)
	public void addConstPerformance_FAIL_MINUS_30_pts(){
		final int SIZE = 65_536;
		final double CONST_FACTOR = 10.0;
		List <Integer> list1 = new LinkedList<>();
		List <Integer> list2 = new ExpressLinkedList<>();

		//"Warm-up"
		for (int i = 0; i < SIZE; i++) list1.add(0, i);
		list1.clear();

		//LinkedList add at the end (benchmark)
		long startTime = System.nanoTime();
		for (int i = 0; i < SIZE; i++){
				list1.add(i);
		}
		long endTime = System.nanoTime();
		long elapsedLL = endTime - startTime;

		//"Warm-up"
		for (int i = 0; i < SIZE; i++) list2.add(0, i);
		list2.clear();

		//ExpressLinkedList add at the beginning
		startTime = System.nanoTime();
		for (int i = 0; i < SIZE; i++){
			list2.add(0, i);
		}
		endTime = System.nanoTime();
		long elapsedELL0 = endTime - startTime;
		
		list2.clear();
		//ExpressLinkedList add at the end
		startTime = System.nanoTime();
		for (int i = 0; i < SIZE; i++){
			list2.add(i, i);
		}
		endTime = System.nanoTime();
		long elapsedELL = endTime - startTime;
		
		System.out.println("Benchmark LL add time (ms): " + elapsedLL/1e6 + "\nELL (ms): " + elapsedELL0/1e6
				+ " : " + elapsedELL/1e6);
		
		assertTrue("Possibly non-constant add() performance for 0/end" , 
				elapsedELL0 < elapsedLL * CONST_FACTOR 
				&& elapsedELL < elapsedLL * CONST_FACTOR);
	}
	
	@Test (timeout = 20000)
	public void getLongJumpsSpeedup_FAIL_MINUS_20_pts(){
//		final int SIZE = 65_536;
		final int SIZE = 51200;
		final double CONST_FACTOR = 3.0;
		List <Integer> list1 = new LinkedList<>();
		List <Integer> list2 = new ExpressLinkedList<>();

		//fill with data
		for (int i = 0; i < SIZE; i++) list1.add(i);
		for (int i = 0; i < SIZE; i++) list2.add(i);

		//LinkedList get (benchmark)
		long startTime = System.nanoTime();
		for (int i = 0; i < SIZE; i++){
		//for (int i = 4000; i < 8000; i++){
				list1.get(i);
		}
		long endTime = System.nanoTime();
		long elapsedLL = endTime - startTime;

		//ExpressLinkedList get
		startTime = System.nanoTime();
		for (int i = 0; i < SIZE; i++){
		//for (int i = 4000; i < 8000; i++){
			list2.get(i);
		}
		endTime = System.nanoTime();
		long elapsedELL = endTime - startTime;
		
		//System.out.println("LinkedList get time (ms): " + elapsedLL/1e6 + "\nExpressLinkedList (ms): " + elapsedELL/1e6);
		System.out.printf("ExpressLinkedList vs LinkedList get() time speedup: %.2fx (%.1fx required)\n", 
				1.0 * elapsedLL/elapsedELL, CONST_FACTOR);
		
		assertTrue("get in ExpressLinkedList must be faster than in LinkedList" , 
				elapsedELL * CONST_FACTOR <= elapsedLL ); 
	}


}
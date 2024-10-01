package com.shoppingmall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
/*
int age = 1;
String content = "testyo";
float floatValue = 3.14159f;
double doubleValue = 3.141592653589793;
System.out.printf("Age: %d, Content: %s", age, content);
System.out.println();
// 소수점 이하 2자리까지 표시
System.out.printf("Formatted float value: %.2f%n", floatValue);
System.out.printf("Formatted double value: %.2f%n", doubleValue);

// 소수점 이하 5자리까지 표시
System.out.printf("Formatted float value: %.5f%n", floatValue);
System.out.printf("Formatted double value: %.5f%n", doubleValue);
*/
class Node {
  int dest, cost;

  public Node(int dest, int cost) {
    this.dest = dest;
    this.cost = cost;
  }
  /* nodes(Node[])   
   * Arrays.toString(nodes)는 '배열의 각 요소'에 대해 toString() 메서드를 호출, 이 메서드를 재정의하지 않으면 기본적 '객체의 메모리 주소'가 출력
  */
  @Override
  public String toString() {
    return "Node{" + "dest=" + dest + ", cost=" + cost + '}';
  }
	  
}
@Slf4j
@SpringBootApplication  
public class ShoppingMallApp {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingMallApp.class, args);
		System.out.println("main!");
		int[] array = {1, 2, 3, 4, 5}; //배열은 고정되어 있음, 동일한 자료형의 요소들만 저장, 연속된 메모리 공간에 저장, 인덱스를 통해요소에 빠르게 접근할 수 있다.
		int[] array2 = new int[] {1, 3, 5, 7, 9};
		int[] array3 = new int[5];
		log.info("============================== 배열 vs List ======================== ");
		System.out.println("array ===>" + Arrays.toString(array));
		System.out.println("array[1] ===>" + array[0]);
		for(int a : array) {
			log.info("a===>" + a);
		}
		
		ArrayList<Integer> arrayList = new ArrayList<>();  // 리스트는 가변 크기이다.필요에 따라 크기가 동적으로 조정된다. 내부적으로 '배열'을 사용 속도는 배열보다 느림
	    List<Integer> intList = new ArrayList<>();
	    List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
	    
		intList.add(0, 0); // intList.add(0);
		intList.add(1, 1); // intList.add(1);
		for(int i : intList) {
			log.info("리스트 반복문 ====>" + i);
		}
	    for(int i = 0; i < intList.size(); i++) {
	    	
	    }
		System.out.println("intList ==> " + intList);
		System.out.println("intList.get(0) ==> " + intList.get(0) + "," + intList.get(1));
		log.info("============================== 해시맵 ======================== ");
		HashMap<String, Integer> hMap = new HashMap<>();
		hMap.put("a", 1);
		hMap.put("b", 2);
		log.info("hashMap ====> " + hMap);  
		if(hMap.containsKey("a")) {
			int value = hMap.get("a");
			log.info("a'value isExist? yes, that's ===> " + value);
		} 
		if(hMap.containsKey("b")) {
			hMap.remove("b");
			log.info("after key 'b' was removed, hMap ===>" + hMap);
		}

		Node[] nodes = new Node[5];
		  nodes[0] = new Node(1, 10);
		  nodes[1] = new Node(2, 20);
		  nodes[2] = new Node(3, 15);
		  nodes[3] = new Node(4, 5);
		  nodes[4] = new Node(1, 25);
		  Arrays.sort(nodes, (o1, o2) -> Integer.compare(o1.cost, o2.cost));  
		  log.info("nodes ====>" + Arrays.toString(nodes));    
		
	}
}

import java.util.*;



public class APABigBase {

	/**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
        Scanner input = null;
	 
        while(true) {
             input = new Scanner(System.in);
             input.useDelimiter(System.getProperty("line.separator"));
             String expr = input.next();
	     //press "Enter" without input, continue
             if (expr.length() == 0) {
            	 continue;
             }
	     //check the exit condition
             else if(expr.length() == 1 && expr.charAt(0) == '0'){
            	 System.out.println("Bye");
            	 break;
             }
             else {
                 try {
                	 long time1 = System.currentTimeMillis()/1000;
                	 AN result = caculateExpression(expr);
                	 long time2 = System.currentTimeMillis()/1000;
                	 System.out.printf("Time: %d\n", time2-time1);
                	 System.out.println(result.toString().substring(0,10));
                 } catch(Exception e) {
                	 System.out.println(e.getMessage());
                 }
             }

        }
        input.close(); 
 }
    public static AN caculateExpression(String ep) throws Exception{
	    //stack1 for storing operands;
	    Stack<AN> stack1 = new Stack<AN>(); 
	    //stack2 for storing operators;
	    Stack<String> stack2 = new Stack<String>(); 
	    //get operands and operators;
	    StringTokenizer tokens = new StringTokenizer(ep, "()+-*^",true);
	    ArrayList<String> tokenList = validateTokens(tokens);
	    LinkedList<String> postfixList = new LinkedList<String>();

	    for(String temp: tokenList) {
	    	//for "+,-", we get left associativity by push "+,-" on top of operators of lower precedence
	    	if (temp.charAt(0) == '+' || temp.charAt(0) == '-'){
	    		while(!stack2.isEmpty()&&(stack2.peek().charAt(0) == '+' ||stack2.peek().charAt(0) == '-' ||
	    				stack2.peek().charAt(0) == '*' || stack2.peek().charAt(0) == '^' )) {
	    			postfixList.add(stack2.pop());
	    		}
	    		stack2.push(temp);
	    	} 
	    	//for '*', we get left associativity by push '*' on top of operators of lower precedence
	    	else if (temp.charAt(0) == '*') {
	    		while(!stack2.isEmpty()&&(stack2.peek().charAt(0) == '*' || stack2.peek().charAt(0) == '^' )) {
	    			postfixList.add(stack2.pop());
	    		}
	    		stack2.push(temp);
	    	} 
	    	//for '^', we get right associativity by push '^' on top of operators of lower and equal precedence
	    	else if(temp.charAt(0) == '^'|| temp.charAt(0) == '(') {
	    		stack2.push(temp);
	    	} 
	    	//on seeing close parenthesis, pop stack until open parenthesis;
	    	else if (temp.charAt(0) == ')') {
	    		while(!stack2.isEmpty()&&stack2.peek().charAt(0) != '(') {
	    			postfixList.add(stack2.pop());
	    		}
	    		if(stack2.isEmpty()) {
	    			throw new Exception("Unmatched parenthesis!");
	    		}
	    		stack2.pop();	
	    	}
	    	else {
	    		postfixList.add(temp);
	    	}
	    }
	    while(!stack2.isEmpty()) {
	    	postfixList.add(stack2.pop());
	    }
	    //Evaluate postfix expression
	    HashSet<Character> operatorSet = new HashSet<Character>();
	    operatorSet.add('+');
	    operatorSet.add('-');
	    operatorSet.add('*');
	//    operatorSet.add('/');
	    operatorSet.add('^');
	//    operatorSet.add('r');
	    for(String item: postfixList) {
	    	//on seeing operands, push them on stack
	    	if(!operatorSet.contains(item.charAt(0))) {
	    		if(item.charAt(0) == '(') {
	    			throw new Exception("Unmatched parenthesis!");
	    		}
	    		else {
	    			stack1.push(new AN(item));
	    		}
	    	}
	    	//on seeing operators, pop enough operands, apply operation, push output onto stack
	    	if(operatorSet.contains(item.charAt(0))) {
	    		performOperation(stack1,item.charAt(0));
	    	}
	    }
	    return stack1.pop();
	}
    
  //Generate a list of validate tokens; including handle negative number;
    public static ArrayList<String> validateTokens(StringTokenizer tokens) throws Exception {
    	ArrayList<String> result = new ArrayList<String>();
    	String currentToken = null;
    	String previousToken = null;
    	//using signSet variable to determine whether currentToken is negative.
    	//if signSet = 1 and currentToken is a number, then currentToken is negative
    	int signSet = 0;
    	while(tokens.hasMoreTokens()) {
    		currentToken = tokens.nextToken().trim();
	    	if(currentToken.equals(""))
	    	    continue;
	    	else if((currentToken.charAt(0)=='-') && 
	    		(previousToken == null || isOperator(previousToken)) ) {
	    		throw new Exception("Negative numbers are not supported");
	    	}
	    	else if((currentToken.charAt(0)=='+') && 
		    	(previousToken == null || isOperator(previousToken)) ) {
		    		signSet = 2;
		    }
	    	else if (Character.isDigit(currentToken.charAt(0)) && signSet == 1) {
	    		result.add(currentToken + '-');
	    		previousToken = currentToken;
	    		if(signSet==2) {
	    			signSet = 0;
	    		}
	    		else
	    			signSet = 2;
	    	}
	    	else if (Character.isDigit(currentToken.charAt(0)) && signSet == 2) {
	    		result.add(currentToken);
	    		previousToken = currentToken;
	    		signSet = 0;
	    	}
	    	else {
	    		result.add(currentToken);
	    		previousToken = currentToken;
	    	}
    	}
    	
    	return result;
    }
    //To determine whether token is an operator
    private static boolean isOperator(String token) {
	    char ops = token.charAt(0);
	    if(ops == '+' || ops == '-'|| ops == '*'||ops == '/'||ops == '^'||ops == 'r'||ops == '('){
	    	return true;
	    }
	    else
	    	return false;
	}

	public static void performOperation(Stack<AN> stack1,
			char operator) throws Exception{
		AN ex1;
		AN ex2;
		try {
			ex1 = stack1.pop();
			ex2 = stack1.pop();
		} catch(EmptyStackException e) {
			throw new Exception("Syntax Error 2");
		}
		
		switch(operator) {
			case '+': 
				stack1.push(ex2.add(ex1));break;
			case '-':
				stack1.push(ex2.substract(ex1));break;
			case '*':
				stack1.push(ex2.multiply(ex1));break;
			case '^':
				stack1.push(ex2.power(ex1));break;
			default:
				throw new Exception("Syntax error: unexpected operator");
		}
		
	}
}

class AN implements Comparable<AN> {
	
	private Node header = new Node();
	private Node tail = header;
	private final int BIG_NUMBER_BASE = 10000;
	private final int DIGIT_NUMBER_EACH_NODE = 4;
	public AN() {
		
	}
	public AN(String s) throws Exception{
		Node current = header;
		// using j to check # of digits <= 8 each node;
		StringBuffer sb = new StringBuffer();
		for(int i = s.length() - 1; i >= 0; i--) {
			
			if (s.charAt(i) == 32) 
				continue;
			else if (s.charAt(i)<48 || s.charAt(i) > 57) {
				throw new Exception("Syntax error1");
			}
			sb.append(s.charAt(i));
			if(sb.length() == DIGIT_NUMBER_EACH_NODE) {
				sb.reverse();
				Node node = new Node(Integer.parseInt(sb.toString()));
				current.next = node;
				current = current.next;
				sb.delete(0, DIGIT_NUMBER_EACH_NODE);
			}
		}
		if(sb.length()!=0) {
			sb.reverse();
			Node node = new Node(Integer.parseInt(sb.toString()));
			current.next = node;
			current = current.next;
		}
		tail = current;
		tailor();
	}
	
	public AN add(AN n) {
		AN n3 = new AN();
		Node p1 = this.header.next;
		Node p2 = n.header.next;
		int carry = 0;
		int temp;
		
		while(p1 != null && p2!=null) {
			if(carry != 0) {
				temp = p1.digit + p2.digit + carry; 
			}
			else {
				temp = p1.digit + p2.digit;
			}
			n3.append(temp % BIG_NUMBER_BASE);
			if (temp >= BIG_NUMBER_BASE)
				carry = 1;
			else
				carry = 0;
			p1 = p1.next;
			p2 = p2.next;
		}
		
		while(p1 != null) {
			if(carry != 0) {
				temp = p1.digit + carry; 
			}
			else {
				temp = p1.digit;
			}
			n3.append(temp % BIG_NUMBER_BASE);
			if (temp >= BIG_NUMBER_BASE)
				carry = 1;
			else
				carry = 0;
			p1 = p1.next;
		}
		
		while(p2 != null) {
			if(carry != 0) {
				temp = p2.digit + carry; 
			}
			else {
				temp = p2.digit;
			}
			n3.append(temp % BIG_NUMBER_BASE);
			if (temp >= BIG_NUMBER_BASE)
				carry = 1;
			else
				carry = 0;
			p2 = p2.next;
		}
		
		if(carry != 0) {
			n3.append(carry); 
		}
		
		return n3;
	}
	
	public AN substract(AN n) throws Exception {
		AN n3 = new AN();
		Node p1 = this.header.next;
		Node p2 = n.header.next;
		int borrow = 0;
		int temp;
		int minuend;
		while(p1 != null && p2!=null) {
			minuend = p1.digit - borrow;
			if((minuend - p2.digit) >= 0) {
				temp = minuend - p2.digit;
			}
			else {
				temp = BIG_NUMBER_BASE + minuend - p2.digit;
				borrow = 1;
			}
			n3.append(temp);
			p1 = p1.next;
			p2 = p2.next;
		}
		while(p1!=null) {
			minuend = p1.digit - borrow;
			if(minuend >= 0) {
				temp = minuend ;
				borrow = 0;
			}
			else {
				temp = BIG_NUMBER_BASE + minuend;
				borrow = 1;
			}
			n3.append(temp);
			p1 = p1.next;
		}
		
		if(p2!=null || borrow ==1){
			throw new Exception("Negative numbers are not supported."); // negative result;
		}
		n3.tailor();
		
		return n3;
	}
	
	public AN power(AN n) throws Exception {
		AN maxInt = new AN(Integer.toString(Integer.MAX_VALUE));
		if(n.compareTo(maxInt)>0)
		{
			AN n1 = new AN("1");
			AN i = new AN();
			for(; i.compareTo(n) < 0; i.increaseByOne()) {
				n1 = n1.multiply(this);
			}
			return n1;
		}
		else {
			int expo = Integer.parseInt(n.toString());
			return power(this, expo); 
		}
			
	}

	public AN power(AN n, int p) throws Exception{
		if(p==0) {
			return new AN("1");
		}
		if(p%2==0) {
			AN n1 = n.multiply(n);
			int p1 = p/2;
			AN n2 = power(n1, p1);
			return n2;
		}
		else {
			AN n1 = n.multiply(n);
			int p1 = p/2;
			AN n2 = power(n1, p1).multiply(n);
			return n2;
		}
	}
	public AN multiply(AN n) {
		AN n3 = new AN();
		Node p1 = this.header.next;
		Node p2 = n.header.next;
		int number = 0;
		while(p2 != null) {
			int carry = 0;
			AN temp = new AN();
			while(p1 != null) {
				temp.append(((p1.digit * p2.digit)% BIG_NUMBER_BASE) + carry);
				carry = p1.digit * p2.digit / BIG_NUMBER_BASE;
				p1 = p1.next;
			}
			if(carry != 0){
				temp.append(carry);
			}
			
			temp.shitLeft(number);
			n3 = n3.add(temp);
			p2 = p2.next;
			p1 = this.header.next;
			number++;
		}
		return n3;
	}
	private AN increaseByOne() {
		/* this private method is an auxiliary function called by power method;*/
		Node current = header.next;
		current.digit += 1;
		while(current.next !=null && current.digit == BIG_NUMBER_BASE ) { 
			current.digit = 0;
			current = current.next;
			current.digit += 1;
		}
		if(current.next ==null && current.digit == BIG_NUMBER_BASE) {
			current.digit = 0;
			current.next = new Node((byte)1);
		}
		return this;
	}
	private AN shitLeft(int n) {
		/* this private method is an auxiliary function called by multiply method; 
		 * add a zero to units digit. */
		int i = 0;
		while(i < n) {
			Node temp = new Node(0);
			temp.next = header.next;
			header.next = temp;
			i++;
		}
		return this;
	}
	@Override
	public int compareTo(AN n) {
		AN n1= new AN();
		try{
			n1 = this.substract(this);
			return n1.toString().equals("0") ? 0:1;
		} catch(Exception e) {
			return -1;
		}
	}
	
	public AN append(int b) {
		tail.next = new Node(b);
		tail = tail.next;
		return this;
	}
	
	
	public void tailor() {
		/* this method is used to deleted meaningless most signifant zeros,
		 * ex.: 0000019929 to 19929*/
		Node r = null;
		Node current = this.header;
		int n = 0;
		while(current.next != null) {
			if(current.next.digit == 0 && n==0) {
				r = current;
				n++;
			}
			else if(current.next.digit == 0 && n!=0) {
				n++;
			}
			else {
				n = 0;
				r = null;
			}
			current = current.next;
		}
		if(r!=null){
			tail = r;
			r.next = null;
		}
	}
	
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();//using StringBuffer object to store temp output
		if(tail == header) return "0";
	       // reverse the digit to output the number	
		Node printHeader = null;
		Node current = header.next;
		while(current !=null) {
			Node temp = new Node(current.digit);
			temp.next = printHeader;
			printHeader = temp;
			current = current.next;
		}
		int i = 0;
		while(printHeader !=null) {
			if(i == 0) {
				sb.append(printHeader.digit);
			}
			else
				sb.append(String.format("%4s", printHeader.digit).replace(' ' ,'0'));
			printHeader = printHeader.next;
			i++;
		}
		return sb.toString();
	}
	
	private class Node {
		/*using int type to store DIGIT_NUMBER_EACH_NODE digits of arbitrary number, Base: 1 billion 
		 *from least significant digit to most significant digit;*/
		int digit; 
		Node next;
		//two overloading constructor
		public Node() {
			digit = 0;
			next = null;
		}
		public Node(int d) {
			digit = d;
			next = null;
		}
	}
}


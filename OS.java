//Author: Dhruvil Patel
//dhruvil_8803

package javafiles;
import java.io.File;
import java.util.*;
class Process{
	int at;
	int bt;
	String pname;
	Process(int at, int bt, String pname){
		this.at = at;
		this.bt = bt;
		this.pname = pname;
	}
	@Override
	public String toString() {
		return at + " " + bt + " " + pname;
	}
}
public class OS {
	static void completetime(ArrayList<String> gantt, int n, int comptime[]) {
		for(int i = gantt.size() - 1; i >= 0; i--) {
			if(gantt.get(i).equals("*")) continue;
			if(comptime[gantt.get(i).charAt(1) - '0' - 1] == 0) comptime[gantt.get(i).charAt(1) - '0' - 1] = i + 1;
		}
	}
	static void FirstCPU(ArrayList<String> gantt, int n , int FirstCPU[]) {
		Arrays.fill(FirstCPU, -1);
		for(int i = 0; i < gantt.size(); i++) {
			if(gantt.get(i).equals("*")) continue;
			if(FirstCPU[gantt.get(i).charAt(1) - '0' - 1] == -1) FirstCPU[gantt.get(i).charAt(1) - '0' - 1] = i;
		}
	}
	static void responsetime(int FirstCPU[], int arrivaltime[], int restime[], int n) {
		for(int i = 0; i < n; i++) {
			restime[i] = FirstCPU[i]- arrivaltime[i];
		}
	}
	static void totaltime(int comptime[], int arrivaltime[], int totaltime[], int n) {
		for(int i = 0; i < n; i++) {
			totaltime[i] = comptime[i]- arrivaltime[i];
		}
	}
	static void waittime(int totaltime[], int busttime[], int waittime[], int n) {
		for(int i = 0; i < n; i++) {
			waittime[i] = totaltime[i]- busttime[i];
		}
	}
	static double avg(int arr[]) {
		double x = 0;
		for(int i = 0; i < arr.length; i++) x += arr[i];
		return (double)x / arr.length;
	}
	static void display(String name, ArrayList<String> gantt, int arrivaltime[], int busttime[], int comptime[], int totaltime[], int waittime[], int restime[], int n) {
		System.out.println("######################### Calculation for " + name +  " ############################");
		System.out.println("\tProcess\tAT\tBT\tCT\tTT\tWT\tRT");
		for(int i = 0; i < n; i++) {
			System.out.println("\tP" + (i +1) + "\t" + arrivaltime[i] + "\t" + busttime[i] + "\t" + comptime[i] + "\t" + 
					totaltime[i] + "\t" + waittime[i] + "\t" + restime[i]);
		}
		System.out.println();
		System.out.println("\tGantt Chart: " + gantt);
		System.out.println("\tAverage Waiting time is: " + avg(waittime));
		System.out.println("\tAverage Turnaround time is: " + avg(totaltime));
		System.out.println("\tAverage Response time is: " + avg(restime));
		System.out.println("\n");
	}
//	Function for First come first serve
	static void FCFS(int arrivalTime[], int busttime[], int n) {
		PriorityQueue<Process> rq= new PriorityQueue<>(new Comparator<Process>() {
			@Override
			public int compare(Process a, Process b) {
				if(a.at <= b.at) return -1;
				else return 1;
			}
		});
		for(int i = 0; i < n; i++) {
			rq.add(new Process(arrivalTime[i], busttime[i], "P" + (i + 1)));
		}
		ArrayList<String> gantt = new ArrayList<>();
		int time = 0;
		while(!rq.isEmpty()) {
			if(rq.peek().at <= time) {
				Process e = rq.poll();
				String proc = e.pname;
				int bt = e.bt;
				while(bt-- > 0) {
					gantt.add(proc);
					time++;
				}
			}
			else {
				gantt.add("*");
				time++;
			}
		}
		int comptime[] = new int[n];
		int totaltime[] = new int[n];
		int waittime[] = new int[n];
		completetime(gantt, n, comptime);
		totaltime(comptime, arrivalTime, totaltime, n);
		waittime(totaltime, busttime, waittime, n);
		int FirstCPU[] = new int[n];
		FirstCPU(gantt, n , FirstCPU);
		int restime[] = new int[n];
		responsetime(FirstCPU, arrivalTime, restime, n);
        display("FCFS", gantt, arrivalTime, busttime, comptime, totaltime, waittime, restime, n);
	}
//	Function for shortest job first
	static void SJF(int arrivalTime[], int busttime[], int n) {
		ArrayList<Process> rq = new ArrayList<>();
		HashSet<Integer> ss = new HashSet<>();
		for(int i = 0; i < n; i++) {
			rq.add(new Process(arrivalTime[i], busttime[i], "P" + (i + 1)));
		}
		ArrayList<String> gantt = new ArrayList<>();
		int time = 0;
		while(ss.size() != n) {
			boolean hachu = false;
			String proc = "";
			int bt = Integer.MAX_VALUE;
			for(int i = 0; i < n; i++) {
				if(ss.contains(i)) continue;
				int x = rq.get(i).at;
				if(x <= time) {
					int y = rq.get(i).bt;
					if(bt > y) {
						bt = y;
						proc = rq.get(i).pname;
						hachu = true;
					}
				}
			}
			if(hachu) {
				while(bt-- > 0) {
					gantt.add(proc);
					time++;
				}
				ss.add(proc.charAt(1) - '0' - 1);
			}
			else {
				gantt.add("*");
				time++;
			}
		}
		int comptime[] = new int[n];
		int totaltime[] = new int[n];
		int waittime[] = new int[n];
		completetime(gantt, n, comptime);
		totaltime(comptime, arrivalTime, totaltime, n);
		waittime(totaltime, busttime, waittime, n);
		int FirstCPU[] = new int[n];
		FirstCPU(gantt, n , FirstCPU);
		int restime[] = new int[n];
		responsetime(FirstCPU, arrivalTime, restime, n);
        display("SJF", gantt, arrivalTime, busttime, comptime, totaltime, waittime, restime, n);
	}
//  Function for shortest remaining time first
	static void SRTF(int arrivalTime[], int busttime[], int n) {
		ArrayList<Process> rq = new ArrayList<>();
		HashSet<Integer> ss = new HashSet<>();
		for(int i = 0; i < n; i++) {
			rq.add(new Process(arrivalTime[i], busttime[i], "P" + (i + 1)));
		}
		ArrayList<String> gantt = new ArrayList<>();
		int time = 0;
		while(ss.size() != n) {
			boolean hachu = false;
			String proc = "";
			int bt = Integer.MAX_VALUE;
			for(int i = 0; i < n; i++) {
				if(ss.contains(i)) continue;
				int x = rq.get(i).at;
				if(x <= time) {
					int y = rq.get(i).bt;
					if(bt > y) {
						bt = y;
						proc = rq.get(i).pname;
						hachu = true;
					}
				}
			}
			if(hachu) {
					gantt.add(proc);
					time++;
					bt--;
					if(bt > 0)
				    rq.get(proc.charAt(1) - '0' - 1).bt--;
					else ss.add(proc.charAt(1) - '0' - 1);
			}
			else {
				gantt.add("*");
				time++;
			}
		}
		int comptime[] = new int[n];
		int totaltime[] = new int[n];
		int waittime[] = new int[n];
		completetime(gantt, n, comptime);
		totaltime(comptime, arrivalTime, totaltime, n);
		waittime(totaltime, busttime, waittime, n);
		int FirstCPU[] = new int[n];
		FirstCPU(gantt, n , FirstCPU);
		int restime[] = new int[n];
		responsetime(FirstCPU, arrivalTime, restime, n);
        display("SRTF", gantt, arrivalTime, busttime, comptime, totaltime, waittime, restime, n);
	}
//	Function for round robin
	static void RR(int arrivalTime[], int busttime[], int n) {
		System.out.println("Enter Time Quanta: ");
		Scanner sc = new Scanner(System.in);
		int quanta = sc.nextInt();
		ArrayList<Process> rq = new ArrayList<>();
		HashSet<Integer> ss = new HashSet<>();
		HashSet<Integer> ssd = new HashSet<>();
		for(int i = 0; i < n; i++) {
			rq.add(new Process(arrivalTime[i], busttime[i], "P" + (i + 1)));
		}
		Queue<Process> e = new LinkedList<>();
		ArrayList<String> gantt = new ArrayList<>();
		int time = 0;
		while(ss.size() != n) {
			for(int i = 0; i < n; i++) {
				if(ssd.contains(i)) continue;
				if(rq.get(i).at <= time) {
					e.add(rq.get(i));
					ssd.add(i);
				}
			}
			if(!e.isEmpty()) {
				  Process z = e.poll();
				   int min = Math.min(z.bt, quanta);
				   String proc = z.pname;
				   int bt = z.bt;
				   while(min-- > 0) {
					   gantt.add(proc);
						time++;
						bt--;   
				   }
				   for(int i = 0; i < n; i++) {
					   if(ssd.contains(i)) continue;
						if(rq.get(i).at <= time) {
							e.add(rq.get(i));
							ssd.add(i);
						}
					}
					if(bt > 0) {
				      z.bt = bt;
				      e.add(z);
				    }
					else ss.add(proc.charAt(1) - '0' - 1);
			}
			else {
				gantt.add("*");
				time++;
			}
		}
		int comptime[] = new int[n];
		int totaltime[] = new int[n];
		int waittime[] = new int[n];
		completetime(gantt, n, comptime);
		totaltime(comptime, arrivalTime, totaltime, n);
		waittime(totaltime, busttime, waittime, n);
		int FirstCPU[] = new int[n];
		FirstCPU(gantt, n , FirstCPU);
		int restime[] = new int[n];
		responsetime(FirstCPU, arrivalTime, restime, n);
        display("Round Robin", gantt, arrivalTime, busttime, comptime, totaltime, waittime, restime, n);
	}
	
	public static void main(String[] args) throws Exception{
		File files = new File("Data.txt");
		Scanner sc = new Scanner(files);
		int n = sc.nextInt();		
		int arrivalTime[] = new int[n];
		int busttime[] = new int[n];
		for(int i = 0; i < n; i++) arrivalTime[i] = sc.nextInt();
		for(int i = 0; i < n; i++) busttime[i] = sc.nextInt();
		loop:
		while(true) {
			System.out.println("############################ CPU SHEDULING ALGORITHMS ###############################");
			System.out.println("\n");
			System.out.println("\t1. FCFS\n"
					+ "\t2. SJF\n"
					+ "\t3. SRTF\n"
					+ "\t4. Round Robin\n"
					+ "\t5. EXIT");
			System.out.println("Enter your choise: ");
			Scanner ff = new Scanner(System.in);
			int choise = ff.nextInt();
			switch(choise) {
			case 1: 
				FCFS(arrivalTime, busttime, n);
				break;
			case 2: 
				SJF(arrivalTime, busttime, n);
				break;
			case 3: 
				SRTF(arrivalTime, busttime, n);
				break;
			case 4: 
				RR(arrivalTime, busttime, n);
				break;
			case 5: 
				break loop;
			}
				
		}
       sc.close();
	}

}
import java.io.*;
import java.util.*;

/**
 * Created by home on 11.02.15.
 */
public class Juicer {
    private ArrayList<String> names;

    public ArrayList<String> getNames() {
       return this.names;
    }
    public void setName(ArrayList<String> names) {
        this.names = names;
    }
    public Juicer creation() {
        Juicer juice = new Juicer();
        ArrayList<String> list = new ArrayList<String> ();
        try {
            BufferedReader buf = new BufferedReader(new FileReader("juice.in"));
            String bufString = "";
            while((bufString = buf.readLine()) != null) {
                list.add(bufString);
            }
            buf.close();
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        juice.setName(list);
        return juice;
    }
    @Override
    public String toString() {
        return (this.names.toString());
    }
    public LinkedHashSet<String> getList() {
        LinkedHashSet<String> set = new LinkedHashSet<String> ();
        for(int i = 0;i < this.names.size();i++) {
            StringTokenizer st = new StringTokenizer(this.names.get(i), " ");
            while (st.hasMoreTokens())
                set.add(st.nextToken());
        }
        return set;
    }
    public void listMentioned() {
        if (!(this.names.isEmpty())) {
            LinkedHashSet<String> set = this.getList();
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("juice1.out"));
                bw.write(set.toString());
                bw.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("juice1.out"));
                bw.write("no elements!");
                bw.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public void sortList() {
        if (!(this.names.isEmpty()))  {
            LinkedHashSet<String> set = this.getList();
            ArrayList<String> list = new ArrayList<String>(set);
            Collections.sort(list, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("juice2.out"));
                bw.write(list.toString());
                bw.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("juice2.out"));
                bw.write("no elements!");
                bw.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public boolean isEntry(ArrayList<String> content,ArrayList<String> store) {
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < store.size(); j++)
            sb.append(store.get(j));
        for (int i = 0; i < content.size(); i++) {
            String check = content.get(i);
            if(sb.indexOf(check) == -1)
                return false;
        }
       return true;
    }
    public ArrayList<ArrayList<String>> swap (ArrayList<ArrayList<String>> list, int firstIndex,int secondIndex) {
        ArrayList<ArrayList<String>> newList = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < list.size(); i++) {
            if (i == firstIndex) {
                newList.add(i, list.get(secondIndex));
            }
            else
            if (i == secondIndex)
                newList.add(i, list.get(firstIndex));
            else
                newList.add(i, list.get(i));
        }
        return newList;
    }
    public void numberMin() {
        if (!(this.names.isEmpty()))  {
            LinkedHashSet<ArrayList<String>> newNames = new LinkedHashSet<ArrayList<String>>();
            for(int i = 0;i < this.names.size();i++) {
                ArrayList<String> list = new ArrayList<String> ();
                StringTokenizer st = new StringTokenizer(this.names.get(i), " ");
                while (st.hasMoreTokens()) {
                    list.add(st.nextToken());
                }
                Collections.sort(list, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                newNames.add(list);
            }
            ArrayList<ArrayList<String>> name = new ArrayList<ArrayList<String>> ();
            for(ArrayList<String> a : newNames) {
                name.add(a);
            }
            for(int i = name.size() - 1;i >= 0; i--)
                for (int j = 0; j < i; j++)
                    if (name.get(j).size() > name.get(j + 1).size()) {
                        name = swap(name, j, j + 1);
                    }
            int count;
            for(int i = 0;i < name.size() - 1;i++) {
                count = i;
                for (int j = count + 1; j < name.size(); j++) {
                    if (isEntry(name.get(i), name.get(j)))
                        name = swap(name, ++count, j);
                }
                i = count++;
            }
            count = 0;
            for(int i = 0;i < name.size() - 1;i++)
                if(!(isEntry(name.get(i),name.get(i + 1))))
                    count++;
            count++;
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("juice3.out"));
                bw.write(Integer.toString(count));
                bw.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        else {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("juice3.out"));
                bw.write("no elements!");
                bw.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

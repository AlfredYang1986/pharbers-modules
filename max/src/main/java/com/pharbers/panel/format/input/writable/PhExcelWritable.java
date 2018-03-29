package com.pharbers.panel.format.input.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import com.pharbers.panel.format.input.writable.writableStrategy.PhPanelStrategy;

public abstract class PhExcelWritable implements WritableComparable<PhExcelWritable>, PhPanelStrategy , java.io.Serializable {
    protected final String delimiter = String.valueOf((char)31);

    private String values = null; // new ArrayList<String>();

    @Override
    public String toString() {
        String result = values;
        return result;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        values = in.readUTF();
    }

    @Override
    public void write(DataOutput out) throws IOException {
//        System.out.println(values);
        out.writeUTF(values);
    }

    @Override
    public int compareTo(PhExcelWritable o) {
        return this.values.hashCode() - o.values.hashCode();
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    /** A Comparator that compares serialized . */
    public static class Comparator extends WritableComparator {
        public Comparator() {
            super(PhExcelWritable.class);
        }

        public int compare(byte[] b1, int s1, int l1,
                           byte[] b2, int s2, int l2) {
            return compareBytes(b1, s1, l1, b2, s2, l2);
        }
    }

    static { // register this comparator
        WritableComparator.define(PhExcelWritable.class, new Comparator());
    }




    protected String mkString(String[] lst, String seq) {
        StringBuffer temp = new StringBuffer();
        for (String i : lst) {
            temp.append(i).append(seq);
        }
        return temp.deleteCharAt(temp.length() - 1).toString();
    }

    protected String[] splitValues(String value) {
        return value.split(delimiter);
    }

    protected String transTitle2Eng(Map<String, String> titleMap, String value) {
        String[] lst = this.splitValues(value);
        List<String> result = new ArrayList<String>(lst.length);
        StringBuffer reVal = new StringBuffer();
        for (String iter : lst) {
            result.add(titleMap.get(iter));
        }
        for (String iter : result) {
            reVal.append(iter).append(delimiter);
        }
        return reVal.deleteCharAt(reVal.length() - 1).toString();
    }

    protected String fullTail(String value, int tl) {
        String[] rLst = value.split(delimiter);
        int rl = rLst.length;
        String[] resultLst = new String[tl];

        for (int i = 0; i < rl; i++) {
            resultLst[i] = rLst[i];
        }

        for (int i = 0; i < tl - rl; i++) {
            resultLst[rl + i] = " ";
        }
        return mkString(resultLst, delimiter);
    }
}

package com.pharbers.panel.format.input.writable;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pharbers.panel.format.input.writable.PhExcelWritableConf;

public class PhExcelWritable implements WritableComparable<PhExcelWritable> {

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
        System.out.println(values);
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
}

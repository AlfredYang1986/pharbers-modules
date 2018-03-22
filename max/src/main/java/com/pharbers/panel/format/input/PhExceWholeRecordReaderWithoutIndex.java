package com.pharbers.panel.format.input;

import com.pharbers.panel.format.input.writable.PhExcelWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import scala.collection.JavaConversions;

import java.io.IOException;
import java.util.List;

class PhExceWholeRecordReaderWithoutIndex extends RecordReader<NullWritable, PhExcelWritable> {

    private FileSplit fileSplit;
    private Configuration conf;
    private String value = null; //new ArrayList<String>();
    private Boolean processed = false;

    private PhExcelHadoop parser = null;

    @Override
    public void initialize(InputSplit inputSplit,
                           TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

        this.fileSplit = (FileSplit)inputSplit;
        this.conf = taskAttemptContext.getConfiguration();

        parser = PhExcelHadoop.apply(((FileSplit) inputSplit).getPath().toUri().getPath(), 0);
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {

        try {
            value = "";
            List<String> tmp = JavaConversions.seqAsJavaList(parser.currentRowData());
            for (String iter : tmp) {
                value += iter + String.valueOf((char)31);
            }
            parser.nextRow();
            return true;

        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        PhExcelWritable reVal = new PhExcelWritable();
        reVal.setValues(value);
        return reVal;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
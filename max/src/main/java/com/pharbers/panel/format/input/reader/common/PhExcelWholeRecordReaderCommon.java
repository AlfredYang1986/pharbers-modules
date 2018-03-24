package com.pharbers.panel.format.input.reader.common;

import java.util.List;
import java.io.IOException;
import scala.collection.JavaConversions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
//import com.pharbers.paction.format.input.PhExcelXLS;
import com.pharbers.paction.format.input.PhExcelXLSX;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * Created by clock on 18-3-24.
 */
public abstract class PhExcelWholeRecordReaderCommon<KEYIN, VALUEIN> extends RecordReader<KEYIN, VALUEIN> {
    protected FileSplit fileSplit;
    protected Configuration conf;
    protected String value = null;
    protected Boolean processed = false;

//    protected PhExcelXLS parser = null;
    protected PhExcelXLSX parser = null;
    protected String delimiter = String.valueOf((char)31);

    @Override
    public void initialize(InputSplit inputSplit,
                           TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

        this.fileSplit = (FileSplit)inputSplit;
        this.conf = taskAttemptContext.getConfiguration();

        /**
         * TODO: 这里以后要分开，XLS的掉用 PhExcelXLS, XLSX的调用 PhExcelXLSX @齐
         */
//        parser = PhExcelXLS.apply(((FileSplit) inputSplit).getPath().toUri().getPath(), 0);
        parser = PhExcelXLSX.apply(((FileSplit) inputSplit).getPath().toUri().getPath(), 0);
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {

        try {
            value = "";
            List<String> tmp = JavaConversions.seqAsJavaList(parser.currentRowData());
            for (String iter : tmp) {
                value += iter + delimiter;
            }

            if (!value.isEmpty())
                value = value.substring(0, value.length() - 1);

            parser.nextRow();
            return true;

        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}

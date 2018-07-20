package com.wu.suchart;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "sufadi";
    private Button btn_real, btn_real_del, chart_clear, btn_chart_none, btn_demo_add, btn_demo_remove;
    private LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValue();
        initListener();
    }

    private void initView() {
        btn_real = (Button) findViewById(R.id.btn_real);
        chart_clear = (Button) findViewById(R.id.chart_clear);
        btn_real_del = (Button) findViewById(R.id.btn_real_del);
        mLineChart = (LineChart) findViewById(R.id.lineChart);
        btn_demo_add = (Button) findViewById(R.id.btn_demo_add);
        btn_demo_remove = (Button) findViewById(R.id.btn_demo_remove);
        btn_chart_none = (Button) findViewById(R.id.btn_chart_none);
    }

    private void initValue() {
        initLineChart();
        initXAxis();
        initYAxisRight();
        initYAxisLeft();
        initLeftYAxisLimitLine();
    }

    private void initListener() {
        btn_real.setOnClickListener(this);
        btn_real_del.setOnClickListener(this);
        chart_clear.setOnClickListener(this);
        btn_chart_none.setOnClickListener(this);
        btn_demo_add.setOnClickListener(this);
        btn_demo_remove.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_real:
            addEntry();
            break;
        case R.id.btn_real_del:
            removeEntry();
            break;
        case R.id.chart_clear:
            clearChart();
            break;
        case R.id.btn_chart_none:
            setEmptyLineData();
            break;
        case R.id.btn_demo_add:
            addDataSet();
            break;
        case R.id.btn_demo_remove:
            removeLastDataSet();
            break;
        default:
            break;
        }

    }

    private void initLineChart() {
        // 显示边界
        mLineChart.setDrawBorders(true);
        // 背景颜色
        mLineChart.setBackgroundColor(0x80000000);
        // 网格背景
        mLineChart.setGridBackgroundColor(0x80000000);
        // 启用/禁用与图表的所有可能的触摸交互。
        // mLineChart.setTouchEnabled(false);
        // 设定x轴最大可见区域范围的大小
        mLineChart.setVisibleXRangeMaximum(5);
        // 设定y轴最大可见区域范围的大小
        mLineChart.setVisibleYRangeMaximum(100f, YAxis.AxisDependency.LEFT);
        mLineChart.setVisibleXRangeMinimum(0f);

        // 设置标签内容
        mLineChart.setDescription("");
        // 设置标签颜色
        mLineChart.setDescriptionColor(Color.WHITE);

        // 为chart添加空数据
        mLineChart.setData(new LineData());
    }

    private void initXAxis() {
        // 得到X轴
        XAxis xAxis = mLineChart.getXAxis();
        // 得到X轴的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // 设置X轴间隔
        Log.d("sufadi", "getSpaceBetweenLabels 1 = " + xAxis.getSpaceBetweenLabels());
        //xAxis.setSpaceBetweenLabels(2);
        // 绘制网格线
        xAxis.setDrawGridLines(true);
        // 设置标签文本
        xAxis.setTextColor(Color.WHITE);

        // 设置文本显示
        xAxis.setValueFormatter(new XAxisValueFormatter() {

            @Override
            public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
                // Log.d("sufadi", "original = " + original + ", index = " +
                // index);
                return String.format("%d s", index);
            }
        });
    }

    private void initYAxisRight() {
        // 得到Y轴右侧并不显示
        YAxis rightYAxis = mLineChart.getAxisRight();
        rightYAxis.setEnabled(false);
    }

    private void initYAxisLeft() {
        // 得到Y轴左侧
        YAxis leftYAxis = mLineChart.getAxisLeft();
        // 设置标签文本
        leftYAxis.setTextColor(Color.WHITE);
        // 说明文字绘图外侧显示
        leftYAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftYAxis.setValueFormatter(new YAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.format("%d ℃", (int) value);
            }
        });
        // Y轴总会高出X轴一点，并没有从0点开始，因此需要对Y轴进行设置
        leftYAxis.setAxisMinValue(0f);
    }

    private void initLeftYAxisLimitLine() {
        // Y轴添加限制线
        LimitLine limitLine = new LimitLine(40, "高温"); // 得到限制线
        limitLine.setTextColor(Color.RED); // 颜色
        limitLine.setLineColor(Color.RED);
        mLineChart.getAxisLeft().addLimitLine(limitLine);
    }

    /**
     * 获取最后一个LineDataSet的索引
     */
    private int getLastDataSetIndex(LineData lineData) {
        int dataSetCount = lineData.getDataSetCount();
        return dataSetCount > 0 ? (dataSetCount - 1) : 0;
    }

    /**
     * 折线图的线条设置
     */
    private LineDataSet createLineDataSet() {
        LineDataSet mLineDataSet = new LineDataSet(null, "实际温度");
        // 以左边坐标轴为准
        mLineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        // 设置文本大小
        mLineDataSet.setValueTextSize(12f);
        // 设置曲线值的圆点是实心还是空心
        mLineDataSet.setDrawCircleHole(false);
        // 设置曲线值的圆点大小
        mLineDataSet.setCircleSize(0);
        // 线模式为圆滑曲线（默认折线）
        mLineDataSet.setDrawCubic(true);
        // 折线是否显示数值
        mLineDataSet.setDrawValues(false);
        // 折线颜色
        mLineDataSet.setColor(Color.YELLOW);
        return mLineDataSet;
    }

    private void clearChart() {
        mLineChart.clear();
    }

    public void setEmptyLineData() {
        mLineChart.setData(new LineData());
        mLineChart.invalidate();
    }

    /**
     * 新增一条数据
     */
    private void addEntry() {
        LineData lineData = mLineChart.getData();

        if (lineData != null) {
            int indexLast = getLastDataSetIndex(lineData);
            LineDataSet lastSet = lineData.getDataSetByIndex(indexLast);
            // set.addEntry(...); // can be called as well

            if (lastSet == null) {
                lastSet = createLineDataSet();
                lineData.addDataSet(lastSet);
            }
            // 这里要注意，x轴的index是从零开始的
            // 假设index=2，那么getEntryCount()就等于3了
            int count = lastSet.getEntryCount();
            // add a new x-value first 这行代码不能少
            lineData.addXValue(count + "");

            float yValues = (float) (Math.random() * 60 + 10);
            // 位最后一个DataSet添加entry
            lineData.addEntry(new Entry(yValues, count), indexLast);
            mLineChart.notifyDataSetChanged();
            mLineChart.moveViewTo(yValues, count, YAxis.AxisDependency.LEFT);
            //mLineChart.setVisibleXRangeMaximum(5);
            //mLineChart.moveViewToX(lastSet.getEntryCount());
            Log.d(TAG, "set.getEntryCount()=" + lastSet.getEntryCount() + " ; indexLastDataSet=" + indexLast);

        }
    }

    /**
     * 移除上一次的数据
     */
    private void removeEntry() {
        LineData lineData = mLineChart.getLineData();
        if (lineData != null) {
            int indexLastDataSet = lineData.getDataSetCount() - 1;
            LineDataSet lastDataSet = lineData.getDataSetByIndex(indexLastDataSet);
            if (lastDataSet != null) {
                Entry lastEntry = lastDataSet.getEntryForXIndex(lastDataSet.getEntryCount() - 1);
                lineData.removeEntry(lastEntry, indexLastDataSet);
                // or remove by index
                // mData.removeEntry(xIndex, dataSetIndex);

                mLineChart.notifyDataSetChanged();
                mLineChart.invalidate();
            }
        }
    }

    /**
     * 新增一组模拟数据
     */
    private void addDataSet() {
        int[] mColors = new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.CYAN, Color.GRAY };
        LineData lineData = mLineChart.getLineData();
        if (lineData != null) {
            int count = (lineData.getDataSetCount() + 1);
            // create 10 y-value
            ArrayList<Entry> yValueList = new ArrayList<Entry>();
            if (lineData.getXValCount() == 0) {
                for (int i = 0; i < 60; i++) {
                    lineData.addXValue((i + 1) + "");
                }
            }

            for (int i = 0; i < lineData.getXValCount(); i++) {
                yValueList.add(new Entry((float) (Math.random() * 60f + 10), i));
            }

            LineDataSet set = new LineDataSet(yValueList, "模拟数据 " + count);
            // 求余，防止数组越界异常
            int colorIndex = count % mColors.length;
            set.setColor(mColors[colorIndex]);
            set.setCircleColor(mColors[colorIndex]);
            set.setValueTextColor(mColors[colorIndex]);
            // 设置曲线值的圆点是实心还是空心
            set.setDrawCircleHole(false);
            // 设置曲线值的圆点大小
            set.setCircleSize(0);
            // 线模式为圆滑曲线（默认折线）
            set.setDrawCubic(true);
            // 折线是否显示数值
            set.setDrawValues(false);
            lineData.addDataSet(set);
            mLineChart.notifyDataSetChanged();
            mLineChart.invalidate();
        }
    }

    /**
     * 移除一组模拟数据
     */
    private void removeLastDataSet() {
        LineData lineData = mLineChart.getData();
        if (lineData != null) {
            lineData.removeDataSet(lineData.getDataSetCount() - 1);
            mLineChart.notifyDataSetChanged();
            mLineChart.invalidate();
        }
    }
}

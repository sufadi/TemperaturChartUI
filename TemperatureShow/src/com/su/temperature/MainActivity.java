package com.su.temperature;

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
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "sufadi";

    private final static int Visible_XRange_Maximum = 60;

    private final static int TYPE_REAL = 0;
    private final static int TYPE_PREDICT = 1;

    private Button btn_clear;
    private Button btn_auto;
    private Button btn_real_add;
    private Button btn_predict_add;
    private LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValue();
        initLisener();
    }

    private void initView() {
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_auto = (Button) findViewById(R.id.btn_auto);
        btn_real_add = (Button) findViewById(R.id.btn_real_add);
        btn_predict_add = (Button) findViewById(R.id.btn_predict_add);
        mLineChart = (LineChart) findViewById(R.id.lineChart);
    }

    private void initValue() {
        initLineChart();
        initXAxis();
        initYAxisRight();
        initYAxisLeft();
        initLeftYAxisLimitLine();
    }

    private void initLisener() {
        btn_real_add.setOnClickListener(this);
        btn_predict_add.setOnClickListener(this);
        btn_auto.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_real_add:
            addEntry(TYPE_REAL);
            break;
        case R.id.btn_predict_add:
            addEntry(TYPE_PREDICT);
            break;
        case R.id.btn_auto:
            feedMultiple();
            break;
        case R.id.btn_clear:
            clear();
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
        mLineChart.setTouchEnabled(false);
        // 设定x轴最大可见区域范围的大小
        mLineChart.setVisibleXRangeMaximum(Visible_XRange_Maximum);
        // 设定y轴最大可见区域范围的大小
        mLineChart.setVisibleYRangeMaximum(100f, YAxis.AxisDependency.LEFT);
        mLineChart.setVisibleXRangeMinimum(0f);

        // 设置标签内容
        mLineChart.setDescription("");
        // 设置标签颜色
        mLineChart.setDescriptionColor(Color.WHITE);

        // 为chart添加空数据
        mLineChart.setData(getDefaultData());
    }

    private LineData getDefaultData() {
        LineData mLineData = new LineData();
        return mLineData;
    }

    private void initXAxis() {
        // 得到X轴
        XAxis xAxis = mLineChart.getXAxis();
        // 得到X轴的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // 设置X轴间隔
        xAxis.setSpaceBetweenLabels(10);
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
        leftYAxis.setAxisMaxValue(100f);
        leftYAxis.setAxisMinValue(0f);
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

    /**
     * 限制线
     */
    private void initLeftYAxisLimitLine() {
        // Y轴添加限制线
        LimitLine limitLine = new LimitLine(40, "");
        limitLine.setTextColor(Color.RED); // 颜色
        limitLine.setLineColor(Color.RED);
        mLineChart.getAxisLeft().addLimitLine(limitLine);
    }

    /**
     * 折线图的线条设置
     */
    private LineDataSet createLineDataSet(int Type) {
        LineDataSet mLineDataSet = new LineDataSet(null, "实际温度");
        switch (Type) {
        case TYPE_PREDICT:
            mLineDataSet.setLabel("预测温度");
            mLineDataSet.setColor(Color.GREEN);
            break;
        case TYPE_REAL:
            mLineDataSet.setLabel("实际温度");
            mLineDataSet.setColor(Color.YELLOW);
            break;

        default:
            break;
        }
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
        return mLineDataSet;
    }

    private void addEntry(int Type) {
        LineData lineData = mLineChart.getData();

        if (null == lineData) {
            return;
        }

        LineDataSet dataSet = null;
        switch (Type) {
        case TYPE_PREDICT:
            dataSet = lineData.getDataSetByLabel("预测温度", true);
            break;
        case TYPE_REAL:
            dataSet = lineData.getDataSetByLabel("实际温度", true);
            break;

        default:
            break;
        }

        if (dataSet == null) {
            dataSet = createLineDataSet(Type);
            lineData.addDataSet(dataSet);
        }

        // 这里要注意，x轴的index是从零开始的
        // 假设index=2，那么getEntryCount()就等于3了
        int count = dataSet.getEntryCount();
        // add a new x-value first 这行代码不能少
        lineData.addXValue(count + "");

        float yValues = (float) (Math.random() * 60 + 10);
        lineData.addEntry(new Entry(yValues, count), lineData.getIndexOfDataSet(dataSet));
        mLineChart.notifyDataSetChanged();
        mLineChart.setVisibleXRangeMaximum(Visible_XRange_Maximum);
        mLineChart.moveViewToX(dataSet.getEntryCount());
        mLineChart.moveViewTo(dataSet.getEntryCount() - Visible_XRange_Maximum, 55f, AxisDependency.LEFT);
        Log.d(TAG, "set.getEntryCount()=" + dataSet.getEntryCount() + ", lineData.getXValCount() = " + lineData.getXValCount());
    }

    private Thread thread;
    private boolean isReturn = false;

    private void feedMultiple() {
        if (thread != null)
            thread.interrupt();

        isReturn = false;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isReturn = !isReturn;
                if (isReturn) {
                    addEntry(TYPE_REAL);
                } else {
                    addEntry(TYPE_PREDICT);
                }
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    // Don't generate garbage runnables inside the loop.
                    runOnUiThread(runnable);
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }
    
    private void clear() {
        mLineChart.clear();
        mLineChart.setData(new LineData());
        mLineChart.invalidate();
        initLineChart();
    }
}

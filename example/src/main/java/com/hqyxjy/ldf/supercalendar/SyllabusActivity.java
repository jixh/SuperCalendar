package com.hqyxjy.ldf.supercalendar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.ldf.calendar.DateUtils;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.model.WeekDate;
import com.ldf.calendar.view.MonthPager;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ldf on 16/11/4.
 */

public class SyllabusActivity extends AppCompatActivity {
    //为什么不使用ButterKnife，是不想让用户看到源码是产生疑问
    TextView textViewYearDisplay;
    TextView textViewMonthDisplay;
    TextView backToday;
    CoordinatorLayout content;
    MonthPager monthPager;
    TextView themeSwitch;
    TextView nextMonthBtn;
    TextView lastMonthBtn;

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private WeekDate updateWeekDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        context = this;
        content = (CoordinatorLayout) findViewById(R.id.content);
        monthPager = (MonthPager) findViewById(R.id.calendar_view);
        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
        monthPager.setViewheight(Utils.dpi2px(context, 270));
        textViewYearDisplay = (TextView) findViewById(R.id.show_year_view);
        textViewMonthDisplay = (TextView) findViewById(R.id.show_month_view);
        backToday = (TextView) findViewById(R.id.back_today_button);
        themeSwitch = (TextView) findViewById(R.id.theme_switch);
        nextMonthBtn = (TextView) findViewById(R.id.next_month);
        lastMonthBtn = (TextView) findViewById(R.id.last_month);
        initCurrentDate();
        initCalendarView();
        initToolbarClickListener();
    }

    /**
     * onWindowFocusChanged回调时，将当前月的种子日期修改为今天
     *
     * @return void
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager();
            initiated = true;
        }
    }

    /**
     * 初始化对应功能的listener
     *
     * @return void
     */
    private void initToolbarClickListener() {
        backToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackToDayBtn();
            }
        });

        themeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DateUtils.isNotMaxMonth(currentDate.toString())){
                monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
                }
                Log.e("Month","nextdate="+currentDate);
            }
        });
        lastMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DateUtils.isNotMinMonth(currentDate.toString())){
                   monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
                }

                Log.e("Month","lastdate="+currentDate);

            }
        });
    }

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        textViewYearDisplay.setText(currentDate.getYear() + "年");
        textViewMonthDisplay.setText(currentDate.getMonth() + "");
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     *
     * @return void
     */
    private void initCalendarView() {
        initListener();

        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);

        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.CalendayType.MONTH,
                customDayView);

        initMonthPager();


        Utils.weekDateList.add(new WeekDate("2017-10-9","2017-10-15"));
        Utils.weekDateList.add(new WeekDate("2017-10-23","2017-10-29"));
        Utils.weekDateList.add(new WeekDate("2017-10-30","2017-11-5"));
        Utils.weekDateList.add(new WeekDate("2017-12-04","2017-12-10"));
        Utils.setSelectDates();
    }

    /**
     * 初始化标记数据，HashMap的形式，可自定义
     *
     * @return void
     */
    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2017-8-9", "1");
        markData.put("2017-7-9", "0");
        markData.put("2017-6-9", "1");
        markData.put("2017-6-10", "0");
        calendarAdapter.setMarkData(markData);
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date,boolean isExist) {
                if (isExist)
                    showDialog(date);
                else if (!TextUtils.isEmpty(Utils.pressedStateStartDate)){

                    add();

                    delete(new CalendarDate(updateWeekDate.startDay));

                    Utils.pressedStateStartDate = null;

//                    CalendarViewAdapter.canSelect = false;
                }
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                if (offset>0 && !DateUtils.isNotMaxMonth(currentDate.toString())){
                    return;
                }
                    monthPager.setCurrentItem(monthPager.getCurrentPosition() + offset);
//                monthPager.selectOtherMonth(offset,currentDate);
            }
        };
    }

    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        textViewYearDisplay.setText(date.getYear() + "年");
        textViewMonthDisplay.setText(date.getMonth() + "");
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) instanceof Calendar) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    refreshClickDate(date);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void onClickBackToDayBtn() {
        refreshMonthPager();
    }

    private void refreshMonthPager() {
        refreshClickDate(new CalendarDate());
        calendarAdapter.notifyDataChanged(currentDate);
    }

    private void refreshSelectBackground() {
        ThemeDayView themeDayView = new ThemeDayView(context, R.layout.custom_day_focus);
        calendarAdapter.setCustomDayRenderer(themeDayView);
        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(new CalendarDate());
    }

    private static final String[] items = {"删除","修改"};

    public void showDialog(final CalendarDate date){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    delete(date);
                }else {
                    update(date);
                    CalendarViewAdapter.canSelect = true;
                }
            }
        });
        builder.show();
    }

    public void delete(CalendarDate date){
        WeekDate weekDate = DateUtils.getWeek(date.toString());
        Utils.weekDateList.remove(weekDate);
        Utils.setSelectDates();
        calendarAdapter.notifyDataChanged(date);

        CalendarViewAdapter.saveDate(null);
    }

    public void update(CalendarDate date){
        updateWeekDate = DateUtils.getWeek(date.toString());
        Utils.pressedStateStartDate = updateWeekDate.startDay;
//        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(date);

    }

    private void add() {
        if (CalendarViewAdapter.getSelectDate() == null)return;
        WeekDate weekDate = DateUtils.getWeek(CalendarViewAdapter.getSelectDate().toString());
        Utils.weekDateList.add(weekDate);
        Utils.setSelectDates();
        CalendarViewAdapter.saveDate(null);

        calendarAdapter.notifyDataChanged(currentDate);
    }


}


package com.ldf.calendar.component;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;

import com.ldf.calendar.Const;
import com.ldf.calendar.DateUtils;
import com.ldf.calendar.Utils;
import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.utils.DrawSelectHelper;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.Day;
import com.ldf.calendar.view.Week;

/**
 * Created by ldf on 17/6/26.
 */

public class CalendarRenderer {
    private Week weeks[] = new Week[Const.TOTAL_ROW];    // 行数组，每个元素代表一行
    private Calendar calendar;
    private CalendarAttr attr;
    private IDayRenderer dayRenderer;
    private Context context;
    private OnSelectDateListener onSelectDateListener;    // 单元格点击回调事件
    private CalendarDate seedDate; //种子日期
    public CalendarDate selectedDate; //被选中的日期
    private int selectedRowIndex = 0;
    private DrawSelectHelper drawSelectHelper;

    public CalendarRenderer(Calendar calendar, CalendarAttr attr, Context context) {
        this.calendar = calendar;
        this.attr = attr;
        this.context = context;
        drawSelectHelper = new DrawSelectHelper();
    }

    /**
     * 使用dayRenderer绘制每一天
     *
     * @return void
     */

    public void draw(Canvas canvas) {

        Log.e("drawxx","draw");

        for (int row = 0; row < Const.TOTAL_ROW; row++) {

            if (weeks[row] != null) {

                for (int col = 0; col < Const.TOTAL_COL; col++) {
                    if (weeks[row].days[col] != null) {
                        if (weeks[row].days[col].getState() == State.SELECT) {
                            drawSelectHelper.onDrawSelect(canvas, attr.getCellWidth(), attr.getCellHeight(), col, row,
                                    DateUtils.isExpire(weeks[row].days[col].getDate().toString()),
                                    weeks[row].days[col].getDate().equals(CalendarViewAdapter.loadDate()));
                        }
                    }
                }

                for (int col = 0; col < Const.TOTAL_COL; col++) {
                    if (weeks[row].days[col] != null) {
                        dayRenderer.drawDay(canvas, weeks[row].days[col], true);
                    }
                }
            }

        }

        if (drawSelectHelper.isAnim())
            calendar.postInvalidate();
    }

    /**
     * 点击某一天时刷新这一天的状态
     *
     * @return void
     */
    public void onClickDate(int col, int row) {

        if (col >= Const.TOTAL_COL || row >= Const.TOTAL_ROW)
            return;

        if (weeks[row] != null) {
            if (attr.getCalendarType() == CalendarAttr.CalendayType.MONTH) {


                if (DateUtils.lessThanToday(weeks[row].days[col].getDate().toString())) {
                    return;
                }


                if (CalendarViewAdapter.isSelect(DateUtils.getWeek(weeks[row].days[col].getDate().toString()).startDay)) {

                    if (!TextUtils.isEmpty(Utils.pressedStateStartDate)) return;

                    onSelectDateListener.onSelectDate(weeks[row].days[col].getDate(), true);

                } else if (CalendarViewAdapter.canSelect) {

                    if (weeks[row].days[col].getState() == State.CURRENT_MONTH) {
                        weeks[row].days[col].setState(State.SELECT);
                        selectedDate = weeks[row].days[col].getDate();
                        CalendarViewAdapter.saveDate(selectedDate);
                        onSelectDateListener.onSelectDate(selectedDate, false);
                        seedDate = selectedDate;
                    } else if (weeks[row].days[col].getState() == State.PAST_MONTH) {
                        selectedDate = weeks[row].days[col].getDate();
                        CalendarViewAdapter.saveDate(selectedDate);
                        onSelectDateListener.onSelectOtherMonth(-1);
                        onSelectDateListener.onSelectDate(selectedDate, false);
                    } else if (weeks[row].days[col].getState() == State.NEXT_MONTH) {
                        selectedDate = weeks[row].days[col].getDate();
                        CalendarViewAdapter.saveDate(selectedDate);
                        onSelectDateListener.onSelectOtherMonth(1);
                        onSelectDateListener.onSelectDate(selectedDate, false);
                    }

                    drawSelectHelper.setAnim(true);
                }


            } else {
                weeks[row].days[col].setState(State.SELECT);
                selectedDate = weeks[row].days[col].getDate();
                CalendarViewAdapter.saveDate(selectedDate);
                onSelectDateListener.onSelectDate(selectedDate, false);
                seedDate = selectedDate;
            }
        }

    }

    /**
     * 刷新指定行的周数据
     *
     * @param rowIndex 参数月所在年
     * @return void
     */
    public void updateWeek(int rowIndex) {
        CalendarDate currentWeekLastDay;
        if (CalendarViewAdapter.weekArrayType == 1) {
            currentWeekLastDay = Utils.getSaturday(seedDate);
        } else {
            currentWeekLastDay = Utils.getSunday(seedDate);
        }
        int day = currentWeekLastDay.day;
        for (int i = Const.TOTAL_COL - 1; i >= 0; i--) {
            CalendarDate date = currentWeekLastDay.modifyDay(day);
            if (weeks[rowIndex] == null) {
                weeks[rowIndex] = new Week(rowIndex);
            }
            if (weeks[rowIndex].days[i] != null) {
//                if (date.equals(CalendarViewAdapter.loadDate())) {
                if (CalendarViewAdapter.isSelect(date.toString())) {
                    weeks[rowIndex].days[i].setState(State.SELECT);
                    weeks[rowIndex].days[i].setDate(date);

                } else {
                    weeks[rowIndex].days[i].setState(State.CURRENT_MONTH);
                    weeks[rowIndex].days[i].setDate(date);
                }
            } else {
                if (CalendarViewAdapter.isSelect(date.toString())) {
                    weeks[rowIndex].days[i] = new Day(State.SELECT, date, rowIndex, i);
                } else {
                    weeks[rowIndex].days[i] = new Day(State.CURRENT_MONTH, date, rowIndex, i);
                }
            }
            day--;
        }
    }


    public void updateSelectWeek(Week selectWeek, int selectRow) {
        CalendarDate currentWeekLastDay;
        if (CalendarViewAdapter.weekArrayType == 1) {
            currentWeekLastDay = Utils.getSaturday(seedDate);
        } else {
            currentWeekLastDay = Utils.getSunday(seedDate);
        }
        int day = currentWeekLastDay.day;
        for (int i = Const.TOTAL_COL - 1; i >= 0; i--) {
            CalendarDate date = currentWeekLastDay.modifyDay(day);
//            if (selectWeek == null) {
//                break;
//            }
            if (selectWeek.days[i] != null) {
//                if (date.equals(CalendarViewAdapter.loadDate())) {
                selectWeek.days[i].setState(State.SELECT);
                selectWeek.days[i].setDate(date);
//                } else {
//                    selectWeek.days[i].setState(State.CURRENT_MONTH);
//                    selectWeek.days[i].setDate(date);
//                }
            } else {
//                if (date.equals(CalendarViewAdapter.loadDate())) {
                selectWeek.days[i] = new Day(State.SELECT, date, selectRow, i);
//                } else {
//                    selectWeek.days[i] = new Day(State.CURRENT_MONTH, date, selectRow, i);
//                }
            }
//            day--;
        }
    }

    /**
     * 填充月数据
     *
     * @return void
     */
    private void instantiateMonth() {
        int lastMonthDays = Utils.getMonthDays(seedDate.year, seedDate.month - 1);    // 上个月的天数
        int currentMonthDays = Utils.getMonthDays(seedDate.year, seedDate.month);    // 当前月的天数
        int firstDayPosition = Utils.getFirstDayWeekPosition(
                seedDate.year,
                seedDate.month,
                CalendarViewAdapter.weekArrayType);

        int day = 0;
        for (int row = 0; row < Const.TOTAL_ROW; row++) {
            day = fillWeek(lastMonthDays, currentMonthDays, firstDayPosition, day, row);
        }
    }

    /**
     * 填充月中周数据
     *
     * @return void
     */
    private int fillWeek(int lastMonthDays,
                         int currentMonthDays,
                         int firstDayWeek,
                         int day,
                         int row) {
        for (int col = 0; col < Const.TOTAL_COL; col++) {
            int position = col + row * Const.TOTAL_COL;// 单元格位置
            if (position >= firstDayWeek && position < firstDayWeek + currentMonthDays) {
                day++;
                fillCurrentMonthDate(day, row, col);
            } else if (position < firstDayWeek) {
                instantiateLastMonth(lastMonthDays, firstDayWeek, row, col, position);
            } else if (position >= firstDayWeek + currentMonthDays) {
                instantiateNextMonth(currentMonthDays, firstDayWeek, row, col, position);
            }
        }
        return day;
    }

    private void fillCurrentMonthDate(int day, int row, int col) {
        CalendarDate date = seedDate.modifyDay(day);
        if (weeks[row] == null) {
            weeks[row] = new Week(row);
        }
        if (weeks[row].days[col] != null) {
            weeks[row].days[col].setDate(date);
            weeks[row].days[col].setState(State.CURRENT_MONTH);
        } else {
            weeks[row].days[col] = new Day(State.CURRENT_MONTH, date, row, col);
        }

        if (CalendarViewAdapter.isSelect(date.toString())) {
            weeks[row].days[col].setState(State.SELECT);
        }

        if (date.equals(seedDate)) {
            selectedRowIndex = row;
        }
    }

    private void instantiateNextMonth(int currentMonthDays,
                                      int firstDayWeek,
                                      int row,
                                      int col,
                                      int position) {
        CalendarDate date = new CalendarDate(
                seedDate.year,
                seedDate.month + 1,
                position - firstDayWeek - currentMonthDays + 1);
        if (weeks[row] == null) {
            weeks[row] = new Week(row);
        }
        if (weeks[row].days[col] != null) {
            weeks[row].days[col].setDate(date);
            weeks[row].days[col].setState(State.NEXT_MONTH);
        } else {
            weeks[row].days[col] = new Day(State.NEXT_MONTH, date, row, col);
        }


        if (CalendarViewAdapter.isSelect(date.toString())) {
            weeks[row].days[col].setState(State.SELECT);
        }
        // TODO: 17/6/27  当下一个月的天数大于七时，说明该月有六周
//        if(position - firstDayWeek - currentMonthDays + 1 >= 7) { //当下一个月的天数大于七时，说明该月有六周
//        }
    }

    private void instantiateLastMonth(int lastMonthDays, int firstDayWeek, int row, int col, int position) {
        CalendarDate date = new CalendarDate(
                seedDate.year,
                seedDate.month - 1,
                lastMonthDays - (firstDayWeek - position - 1));
        if (weeks[row] == null) {
            weeks[row] = new Week(row);
        }
        if (weeks[row].days[col] != null) {
            weeks[row].days[col].setDate(date);
            weeks[row].days[col].setState(State.PAST_MONTH);
        } else {
            weeks[row].days[col] = new Day(State.PAST_MONTH, date, row, col);
        }


        if (CalendarViewAdapter.isSelect(date.toString())) {
            weeks[row].days[col].setState(State.SELECT);
        }
    }

    /**
     * 根据种子日期孵化出本日历牌的数据
     *
     * @return void
     */
    public void showDate(CalendarDate seedDate) {
        if (seedDate != null) {
            this.seedDate = seedDate;
        } else {
            this.seedDate = new CalendarDate();
        }
        update();
    }

    public void update() {
        instantiateMonth();
        calendar.invalidate();
    }

    public CalendarDate getSeedDate() {
        return this.seedDate;
    }

    public void cancelSelectState() {
        for (int i = 0; i < Const.TOTAL_ROW; i++) {
            if (weeks[i] != null) {
                for (int j = 0; j < Const.TOTAL_COL; j++) {
                    if (weeks[i].days[j].getState() == State.SELECT) {
                        weeks[i].days[j].setState(State.CURRENT_MONTH);
                        resetSelectedRowIndex();
                        break;
                    }
                }
            }
        }
    }

    public void resetSelectedRowIndex() {
        selectedRowIndex = 0;
    }

    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public CalendarAttr getAttr() {
        return attr;
    }

    public void setAttr(CalendarAttr attr) {
        this.attr = attr;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setOnSelectDateListener(OnSelectDateListener onSelectDateListener) {
        this.onSelectDateListener = onSelectDateListener;
    }

    public void setDayRenderer(IDayRenderer dayRenderer) {
        this.dayRenderer = dayRenderer;
    }

}

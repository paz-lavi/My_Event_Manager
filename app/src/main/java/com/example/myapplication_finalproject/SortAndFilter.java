package com.example.myapplication_finalproject;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SortAndFilter {


    public static ArrayList<MyEvent> filterByPhoneNumber(ArrayList<MyEvent> events, String phone) {
        ArrayList<MyEvent> temp = new ArrayList<>();
        for (MyEvent e : events) {
            if (e.getCustomerPhone().equals(phone))
                temp.add(e);
        }
        sortByDate(temp);
        return temp;
    }

    public static ArrayList<MyEvent> filterByDateRange(
            HashMap<String, HashMap<String, HashMap<String, HashMap<String, MyEvent>>>> events, String start, String end) {

        ArrayList<MyEvent> temp = new ArrayList<>();
        String[] d1 = start.split("/");
        String[] d2 = end.split("/");
        int start_year = Integer.parseInt(d1[2]);
        int start_mount = Integer.parseInt(d1[1]);
        int start_day = Integer.parseInt(d1[0]);
        int end_year = Integer.parseInt(d2[2]);
        int end_mount = Integer.parseInt(d2[1]);
        int end_day = Integer.parseInt(d2[0]);

        for (int i = start_year; i <= end_year; i++) {
            for (int j = start_mount; j <= end_mount; j++) {
                for (int k = 1; k <= 31; k++) {
                    if (k < start_day && j == start_mount && i == start_year) {

                    } else {
                        if (j == end_mount && i == end_year && k == end_day)
                            break;
                        try {
                            for (String key : events.get(String.valueOf(i)).get(String.valueOf(j)).get(String.valueOf(k)).keySet()
                            ) {
                                temp.add(events.get(String.valueOf(i)).get(String.valueOf(j)).get(String.valueOf(k)).get(key));
                            }
                        } catch (NullPointerException e) {

                        }

                    }

                }
            }
        }

        sortByDate(temp);
        return temp;
    }


    public static void sortByDate(ArrayList<MyEvent> events) {
        Collections.sort(events, new Comparator<MyEvent>() {
            @Override
            public int compare(MyEvent o1, MyEvent o2) {
                return compareDate(o1.getDate(), o2.getDate());
            }
        });
    }

    public static void sortExpensesByDate(ArrayList<Expenses> expenses) {
        Collections.sort(expenses, new Comparator<Expenses>() {
            @Override
            public int compare(Expenses o1, Expenses o2) {
                return compareDate(o1.getDate(), o2.getDate());
            }
        });
    }

    public static ArrayList<MyEvent> filterByPaid(ArrayList<MyEvent> events) {
        ArrayList<MyEvent> temp = new ArrayList<>();
        for (MyEvent e : events
        ) {
            if (e.getPayment().getAmountleft() == 0)
                temp.add(e);
        }
        return temp;
    }

    public static ArrayList<MyEvent> filterByNotPaid(ArrayList<MyEvent> events) {
        ArrayList<MyEvent> temp = new ArrayList<>();
        for (MyEvent e : events
        ) {
            if (e.getPayment().getAmountleft() != 0)
                temp.add(e);
        }
        return temp;
    }

    public static ArrayList<MyEvent> filterByNotPaidPreviousEvents(ArrayList<MyEvent> events) {
        ArrayList<MyEvent> myEvents = filterByNotPaid(filterByPreviousEvents(events));

        ArrayList<MyEvent> temp = new ArrayList<>();
        for (MyEvent e : myEvents
        ) {
            if (e.getPayment().getAmountleft() != 0)
                temp.add(e);
        }
        return temp;
    }

    public static ArrayList<MyEvent> filterByPaidPreviousEvents(ArrayList<MyEvent> events) {
        ArrayList<MyEvent> myEvents = filterByNotPaid(filterByPreviousEvents(events));

        ArrayList<MyEvent> temp = new ArrayList<>();
        for (MyEvent e : myEvents
        ) {
            if (e.getPayment().getAmountleft() == 0)
                temp.add(e);
        }
        return temp;
    }


    public static ArrayList<MyEvent> filterByFutureEvents(ArrayList<MyEvent> events) {

        return new ArrayList<>(events.subList(findFutureEventsIndex(events), events.size()));


    }


    public static ArrayList<MyEvent> filterByPreviousEvents(ArrayList<MyEvent> events) {
        int i = findFutureEventsIndex(events);
        ArrayList<MyEvent> e = new ArrayList<>(events.subList(0, i == 0 ? 0 : i - 1));
        Collections.reverse(e);
        return e;
    }

    public static ArrayList<MyEvent> filterByFutureConfirmedEvents(ArrayList<MyEvent> events) {
        ArrayList<MyEvent> e = filterByFutureEvents(events);
        ArrayList<MyEvent> res = new ArrayList<>(e);

        for (MyEvent temp : e
        ) {
            if (!temp.getStatus().equals(Constants.STATUS_CONFIRM))
                res.remove(temp);
        }
        return res;
    }

    public static ArrayList<MyEvent> filterByFutureNotConfirmedEvents(ArrayList<MyEvent> events) {
        ArrayList<MyEvent> e = filterByFutureEvents(events);
        ArrayList<MyEvent> res = new ArrayList<>(e);

        for (MyEvent temp : e
        ) {
            if (!temp.getStatus().equals(Constants.STATUS_WAIT_FOR_CONFIRM))
                res.remove(temp);
        }
        return res;
    }

    public static ArrayList<Expenses> expensesFilterByYear(String year,
                                                           HashMap<String, HashMap<String, ArrayList<Expenses>>> ex) {
        if (ex == null)
            return null;
        ArrayList<Expenses> temp = new ArrayList<>();
        if (ex.get(year) != null) {
            for (String key : ex.get(year).keySet()) {
                temp.addAll(ex.get(year).get(key));
            }
        }
        sortExpensesByDate(temp);
        Collections.reverse(temp);
        return temp;
    }

    /**
     * find the index of the first future event
     */
    private static int findFutureEventsIndex(ArrayList<MyEvent> events) {
        SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
        String date = formatter.format(System.currentTimeMillis());
        sortByDate(events);
        int low = 0;
        int high = events.size() - 1;
        int mid = events.size() / 2;
        int index = 0;
        while (low <= high) {
            int res = compareDate(date, events.get(mid).getDate());
            Log.d("compareres", "res = " + res);
            if (res < 0) {
                high = mid - 1;
                mid = (high + low) / 2;
            } else if (res > 0) {
                low = mid + 1;
                index = mid + 1;
                mid = (high + low) / 2;
            } else { // compare date == 0 (found)
                index = mid;
                break;
            }

        }
        if (index > 0) {
            while (compareDate(date, events.get(index - 1).getDate()) <= -1) {// find the first future event
                if (index > 1)
                    index--;
                else break;
            }
        }
        return index;
    }

    /**
     * compare between 2 dates
     */
    private static int compareDate(String d1, String d2) {
        String[] date1 = d1.split("/");
        String[] date2 = d2.split("/");
        int i;
        i = Integer.parseInt(date1[2]) - Integer.parseInt(date2[2]);
        Log.d("compareDate", "year= " + i);
        if (i != 0)
            return i;
        else i = Integer.parseInt(date1[1]) - Integer.parseInt(date2[1]);
        Log.d("compareDate", "mount= " + i);

        if (i != 0)
            return i;

        i = Integer.parseInt(date1[0]) - Integer.parseInt(date2[0]);
        Log.d("compareDate", "day= " + i);
        return i;


    }


}

package kz.damir211.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Widget extends AppWidgetProvider {

    public static StringBuilder fl = new StringBuilder();
    public static StringBuilder sl = new StringBuilder();
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        new GetLines().execute();
        int ids = appWidgetIds.length;
        for (int i = 0; i < ids; i++) {
            int id = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
            views.setTextViewText(R.id.tv1, fl.toString());
            views.setTextViewText(R.id.tv2, sl.toString());
            appWidgetManager.updateAppWidget(id, views);
        }
    }

    class GetLines extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM");
            Date date = new Date();
            String today = df.format(date);
            String firstLine = String.format(" Восход и закат солнца и луны в Павлодаре на (%s):\n", today);
            fl.setLength(0);
            fl.append(firstLine);
            sl.setLength(0);
            sl.append(getRatesData());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public static String getRatesData() {
        StringBuilder data = new StringBuilder();
        try {
            String BASE_URL = "https://world-weather.ru/pogoda/kazakhstan/pavlodar/sunrise/";
            Document doc = Jsoup.connect(BASE_URL).get();
            Elements voshodS = doc.select("dl.bg-sun > dd:first-of-type");
            Elements zakatS = doc.select("dl.bg-sun > dd:last-of-type");
            Elements voshodL = doc.select("dl[class=\"sunset-box-data\"] > dd:first-of-type");
            Elements zakatL = doc.select("dl[class=\"sunset-box-data\"] > dd:last-of-type");
            Elements timeD = doc.select("div[class=\"sunset-box-now day-length\"] > span");
            data.append("Восход солнца: " + voshodS.text()+"\n");
            data.append("Закат солнца: " + zakatS.text()+"\n");
            data.append("Восход луны: " + voshodL.text()+"\n");
            data.append("Закат луны: " + zakatL.text()+"\n");
            data.append("День будет длиться: " + timeD.text()+"\n");
        } catch (Exception ignored) {
            return null; // При ошибке доступа возвращаем null
        }
        return data.toString(); // Возвращаем результат

    }

}

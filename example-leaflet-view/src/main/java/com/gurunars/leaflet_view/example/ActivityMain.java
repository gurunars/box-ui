package com.gurunars.leaflet_view.example;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gurunars.leaflet_view.LeafletView;
import com.gurunars.leaflet_view.NoPageRenderer;
import com.gurunars.leaflet_view.PageRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ActivityMain extends AppCompatActivity {

    private List<TitledPage> pages = new ArrayList<>();
    private LeafletView<View, TitledPage> leafletView;

    private void updateAdapter() {
        Collections.sort(pages, new Comparator<TitledPage>() {
            @Override
            public int compare(TitledPage lhs, TitledPage rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
        leafletView.setPages(pages);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        leafletView = (LeafletView<View, TitledPage>) findViewById(R.id.leafletView);
        leafletView.setPageRenderer(new PageRenderer<View, TitledPage>() {
            @Override
            public View renderPage(TitledPage page) {
                View viewGroup = LayoutInflater.from(ActivityMain.this).inflate(
                        R.layout.page_view, leafletView, false);
                TextView textView = (TextView) viewGroup.findViewById(
                        R.id.pageTitle);
                textView.setText(page.toString());
                viewGroup.setTag(page);
                return viewGroup;
            }

            @Override
            public void enter(View pageView) {
                setTitle(pageView.getTag().toString());
            }

            @Override
            public void leave(View pageView) {

            }
        });

        leafletView.setNoPageRenderer(new NoPageRenderer() {
            @Override
            public View renderNoPage() {
                return LayoutInflater.from(ActivityMain.this).
                        inflate(R.layout.no_page_view, leafletView, false);
            }

            @Override
            public void enter() {
                setTitle(R.string.empty);
            }

        });

        load();
        updateAdapter();
    }

    private void load() {
        pages = new Gson().fromJson(getPreferences(Context.MODE_PRIVATE).getString("data", "[]"), new TypeToken<ArrayList<TitledPage>>(){}.getType());
    }

    private void save() {
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString("data", new Gson().toJson(pages));
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.create:
                createPage();
                return true;
            case R.id.delete:
                deletePage();
                return true;
            case R.id.edit:
                editPage();
                return true;
            case R.id.go_to:
                goToPage();
                return true;
            case R.id.clear:
                clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static class TitledPageAdapter extends ArrayAdapter<TitledPage> {

        TitledPageAdapter(Context context, List<TitledPage> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                TextView text = (TextView) convertView.findViewById(android.R.id.text1);
                text.setText(getItem(position).getTitle());
            }
            return convertView;
        }

    }

    private void goToPage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.go_to);
        builder.setSingleChoiceItems(
                new TitledPageAdapter(this, pages), -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leafletView.goTo(pages.get(which));
                        dialog.dismiss();
                    }
        });
        builder.setCancelable(true);
        builder.show();
    }

    private void editPage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit);
        final EditText input = new EditText(this);
        input.setId(R.id.pageTitle);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        TitledPage currentPage = leafletView.getCurrentPage();
        if (currentPage == null) {
            return;
        }
        input.setText(currentPage.getTitle());
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TitledPage page = leafletView.getCurrentPage();
                page.setTitle(input.getText().toString());
                // NOTE: surely equals method could have been implemented
                // however the idea is to demo that these methods are not important -
                // only getId method is
                for (int i=0; i < pages.size(); i++) {
                    if (pages.get(i).getId() == page.getId()) {
                        pages.set(i, page);
                    }
                }
                updateAdapter();
                save();
            }
        });
        builder.show();
    }

    private void deletePage() {
        // NOTE: surely equals method could have been implemented
        // however the idea is to demo that these methods are not important - only getId method is
        TitledPage page = leafletView.getCurrentPage();
        for (int i=0; i < pages.size(); i++) {
            if (pages.get(i).getId() == page.getId()) {
                pages.remove(i);
            }
        }
        updateAdapter();
        save();
    }

    private void createPage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit);
        final EditText input = new EditText(this);
        input.setId(R.id.pageTitle);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TitledPage page = new TitledPage(input.getText().toString());
                pages.add(page);
                updateAdapter();
                save();
            }
        });
        builder.show();
    }

    private void clear() {
        pages.clear();
        updateAdapter();
        save();
    }

}
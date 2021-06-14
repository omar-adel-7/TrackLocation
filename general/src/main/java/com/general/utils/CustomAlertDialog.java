package com.general.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.general.R;

import static android.app.AlertDialog.THEME_HOLO_LIGHT;

public class CustomAlertDialog {


    String alertnegbutton = "";

    private CustomAlertDialogInterface alertDialogInterface;

    AppCompatActivity activity;
    String alerttitle, alertbutton;

    public CustomAlertDialog(AppCompatActivity activity) {
        this.activity = activity;
        alerttitle = activity.getResources().getString(R.string.alert);
        alertbutton = activity.getResources().getString(R.string.done);
    }


    public CustomAlertDialog(CustomAlertDialogInterface alertDialogInterface, AppCompatActivity activity) {
        this.alertDialogInterface = alertDialogInterface;
        this.activity = activity;
        alerttitle = activity.getResources().getString(R.string.alert);
        alertbutton = activity.getResources().getString(R.string.done);
    }


    public void alertDialog(String alertmessage) {
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                activity, THEME_HOLO_LIGHT)
                .setCancelable(false)
                .setTitle(alerttitle)
                .setMessage(
                        alertmessage)
                .setPositiveButton(alertbutton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alert_builder.show();
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);

    }

    public void alertDialog(String alertmessage, CustomAlertDialogInterface alertDialogInterface) {
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                activity, THEME_HOLO_LIGHT)
                .setCancelable(false)
                .setTitle(alerttitle)
                .setMessage(
                        alertmessage)
                .setPositiveButton(alertbutton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                alertDialogInterface.onPositiveButtonClicked();
                            }
                        });

        AlertDialog alert = alert_builder.show();
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);

    }

    public void alertDialogConfirm(String alertmessage) {

        {
            alerttitle = activity.getResources().getString(R.string.alert);
            alertbutton = activity.getResources().getString(R.string.done2);
            alertnegbutton = activity.getResources().getString(R.string.noalert);
        }

        AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                activity, THEME_HOLO_LIGHT)
                .setCancelable(false)
                .setTitle(alerttitle)
                .setMessage(
                        alertmessage)
                .setPositiveButton(alertbutton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                alertDialogInterface.onPositiveButtonClicked();
                            }
                        })
                .setNegativeButton(alertnegbutton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                dialog.cancel();

                            }
                        });

        AlertDialog alert = alert_builder.show();
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
//        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(android.R.color.holo_red_dark));
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(20);

    }

    public void alertDialogConfirm(String alertmessage, CustomAlertDialogInterface alertDialogInterface) {

        {
            alerttitle = activity.getResources().getString(R.string.alert);
            alertbutton = activity.getResources().getString(R.string.done2);
            alertnegbutton = activity.getResources().getString(R.string.noalert);
        }

        AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                activity, THEME_HOLO_LIGHT)
                .setCancelable(false)
                .setTitle(alerttitle)
                .setMessage(
                        alertmessage)
                .setPositiveButton(alertbutton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                alertDialogInterface.onPositiveButtonClicked();
                            }
                        })
                .setNegativeButton(alertnegbutton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                dialog.cancel();

                            }
                        });

        AlertDialog alert = alert_builder.show();
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
//        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(android.R.color.holo_red_dark));
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(20);

    }

    public interface CustomAlertDialogInterface {

        public void onPositiveButtonClicked();


    }


}

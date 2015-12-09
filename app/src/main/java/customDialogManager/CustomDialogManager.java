package customDialogManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.nganthoi.salai.tabgen.R;

public class CustomDialogManager {
    private Context context;
    private String title;
    private String message;
    private Boolean status,state=false;

    public CustomDialogManager(Context _context,String _title,String _msg,Boolean _status){
        context = _context;
        title = _title;
        message = _msg;
        status = _status;
    }

    public Boolean showCustomDialogWithYesNoButton(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        if(title!=null) alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        if(status){
            alertDialogBuilder.setIcon(R.drawable.success_icon);
        }
        else alertDialogBuilder.setIcon(R.drawable.failure_icon);
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                state=true;
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                state=false;
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return state;
    }

    public void showCustomDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        if(title!=null) alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        if(status){
            alertDialogBuilder.setIcon(R.drawable.success_icon);
        }
        else alertDialogBuilder.setIcon(R.drawable.failure_icon);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

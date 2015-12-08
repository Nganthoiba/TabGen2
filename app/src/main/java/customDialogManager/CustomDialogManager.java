package customDialogManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.nganthoi.salai.tabgen.R;

public class CustomDialogManager {
    private Context context;
    private String title;
    private String message;
    private Boolean status;

    public CustomDialogManager(Context _context,String _title,String _msg,Boolean _status){
        context = _context;
        title = _title;
        message = _msg;
        status = _status;
    }

    public void showCustomDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if(status){
            alertDialog.setIcon(R.drawable.success_icon);
        }
        else alertDialog.setIcon(R.drawable.failure_icon);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}

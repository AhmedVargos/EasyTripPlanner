package fallenleafapps.com.tripplanner.utils;

/**
 * Created by Vargos on 21-Mar-18.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class CustomTripDialog {

    private Context mContext;

    @DrawableRes
    private int mIcon;

    @ColorRes
    private int mIconColor;

    @StringRes
    private String mTitle;

    @DrawableRes
    private int mBackgroundColor;

    @StringRes
    private int mDescription;

    @StringRes
    private int mReviewQuestion;


    private ImageView titleImageView;
    private TextView titleTextView;
    private TextView    descriptionTextView;
    private TextView    reviewQuestionTextView;

    private LinearLayout positiveFeedbackLayout;
    private LinearLayout negativeFeedbackLayout;
    private LinearLayout ambiguityFeedbackLayout;
    private LinearLayout feedbackBodyLayout;

    private TextView positiveFeedbackTextView;
    private TextView negativeFeedbackTextView;
    private TextView ambiguityFeedbackTextView;

    private ImageView positiveFeedbackIconView;
    private ImageView negativeFeedbackIconView;
    private ImageView ambiguityFeedbackIconView;


    @StringRes
    private int mPositiveFeedbackText;

    @DrawableRes
    private int mPositiveFeedbackIcon;

    @StringRes
    private int mNegativeFeedbackText;

    @DrawableRes
    private int mNegativeFeedbackIcon;

    @StringRes
    private int mAmbiguityFeedbackText;

    @DrawableRes
    private int mAmbiguityFeedbackIcon;

    private Dialog mDialog;

    private FeedBackActionsListeners mReviewActionsListener;

    public CustomTripDialog(Context mContext)
    {
        this.mContext = mContext;

        mDialog = new Dialog(mContext, design.ivisionblog.apps.reviewdialoglibrary.R.style.FeedbackDialog_Theme_Dialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(design.ivisionblog.apps.reviewdialoglibrary.R.layout.review_dialog_base);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.50);

            if (mDialog.getWindow() != null) {
                mDialog.getWindow().setLayout(width, height);
            }
        }
    }

    private void initiateAllViews()
    {
        titleImageView          = (ImageView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.review_icon);
        titleTextView           = (TextView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.review_title);
        descriptionTextView     = (TextView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.review_description);
        reviewQuestionTextView  = (TextView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.review_questions);

        feedbackBodyLayout      = (LinearLayout) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.feedback_body_layout);

        positiveFeedbackLayout = (LinearLayout) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.postive_feedback_layout);
        negativeFeedbackLayout = (LinearLayout) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.negative_feedback_layout);
        ambiguityFeedbackLayout = (LinearLayout) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.ambiguity_feedback_layout);


        positiveFeedbackTextView = (TextView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.positive_feedback_text);
        negativeFeedbackTextView = (TextView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.negative_feedback_text);
        ambiguityFeedbackTextView = (TextView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.ambiguity_feedback_text);

        positiveFeedbackIconView = (ImageView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.postive_feedback_icon);
        negativeFeedbackIconView = (ImageView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.negative_feedback_icon);
        ambiguityFeedbackIconView = (ImageView) mDialog.findViewById(design.ivisionblog.apps.reviewdialoglibrary.R.id.ambiguity_feedback_icon);
    }

    private void initiateListeners()
    {

        positiveFeedbackLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onPositiveFeedbackClicked(v);
            }
        });

        negativeFeedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNegativeFeedbackClicked(v);
            }
        });

        ambiguityFeedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAmbiguityFeedbackClicked(v);
            }
        });

        if(mDialog != null)
        {
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    onCancelListener(dialog);
                }
            });
        }
    }

    public CustomTripDialog show()
    {
        if(mDialog != null && mContext != null)
        {
            initiateAllViews();
            initiateListeners();

            LayerDrawable layerDrawable = (LayerDrawable) mContext.getResources().getDrawable(design.ivisionblog.apps.reviewdialoglibrary.R.drawable.reviewdialog_round_icon);
            GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(design.ivisionblog.apps.reviewdialoglibrary.R.id.round_background);
            gradientDrawable.setColor(Color.parseColor("#FFFFFF"));
            layerDrawable.setDrawableByLayerId(design.ivisionblog.apps.reviewdialoglibrary.R.id.round_background,gradientDrawable);

            Drawable drawable = mContext.getResources().getDrawable(this.mIcon);
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                DrawableCompat.setTint(drawable.mutate(), mContext.getResources().getColor(mIconColor));
            }
            else
            {
                drawable.setColorFilter(mContext.getResources().getColor(mIconColor), PorterDuff.Mode.SRC_IN);
            }


            layerDrawable.setDrawableByLayerId(design.ivisionblog.apps.reviewdialoglibrary.R.id.drawable_image,drawable);

            titleImageView.setImageDrawable(layerDrawable);
            titleTextView.setText(mTitle);
            descriptionTextView.setText(mContext.getString(this.mDescription));
            reviewQuestionTextView.setText(mContext.getString(this.mReviewQuestion));

            positiveFeedbackTextView.setText(this.mPositiveFeedbackText);
            positiveFeedbackIconView.setImageResource(this.mPositiveFeedbackIcon);
            positiveFeedbackIconView.setColorFilter(mContext.getResources().getColor(mIconColor));

            negativeFeedbackTextView.setText(this.mNegativeFeedbackText);
            negativeFeedbackIconView.setImageResource(this.mNegativeFeedbackIcon);
            negativeFeedbackIconView.setColorFilter(mContext.getResources().getColor(mIconColor));

            ambiguityFeedbackTextView.setText(this.mAmbiguityFeedbackText);
            ambiguityFeedbackIconView.setImageResource(this.mAmbiguityFeedbackIcon);
            ambiguityFeedbackIconView.setColorFilter(mContext.getResources().getColor(mIconColor));

            feedbackBodyLayout.setBackgroundResource(this.mBackgroundColor);

            mDialog.show();
        }
        return this;
    }

    public int getTitleIcon()
    {
        return mIcon;
    }

    public CustomTripDialog setIcon(int mIcon)
    {
        this.mIcon =  mIcon;
        return this;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public CustomTripDialog setTitle(String mTitle)
    {
        this.mTitle = mTitle;
        return this;
    }

    public int getDescription()
    {
        return mDescription;
    }

    public CustomTripDialog setDescription(int mDescription)
    {
        this.mDescription = mDescription;
        return this;
    }

    public int getPositiveFeedbackText()
    {
        return mPositiveFeedbackText;
    }

    public CustomTripDialog setPositiveFeedbackText(@StringRes int mPositiveFeedbackText)
    {
        this.mPositiveFeedbackText = mPositiveFeedbackText;
        return this;
    }

    public int getPositiveFeedbackIcon()
    {
        return mPositiveFeedbackIcon;
    }

    public CustomTripDialog setPositiveFeedbackIcon(@DrawableRes int mPositiveFeedbackIcon)
    {
        this.mPositiveFeedbackIcon = mPositiveFeedbackIcon;
        return this;
    }

    public int getNegativeFeedbackText()
    {
        return mNegativeFeedbackText;
    }

    public CustomTripDialog setNegativeFeedbackText(@StringRes int mNegativeFeedbackText)
    {
        this.mNegativeFeedbackText = mNegativeFeedbackText;
        return this;
    }

    public int getNegativeFeedbackIcon()
    {
        return mNegativeFeedbackIcon;
    }

    public CustomTripDialog setNegativeFeedbackIcon(@DrawableRes int mNegativeFeedbackIcon)
    {
        this.mNegativeFeedbackIcon = mNegativeFeedbackIcon;
        return this;
    }

    public int getAmbiguityFeedbackText()
    {
        return mAmbiguityFeedbackText;
    }

    public CustomTripDialog setAmbiguityFeedbackText(@StringRes int mAmbiguityFeedbackText)
    {
        this.mAmbiguityFeedbackText = mAmbiguityFeedbackText;
        return this;
    }

    public int getAmbiguityFeedbackIcon()
    {
        return mAmbiguityFeedbackIcon;
    }

    public CustomTripDialog setAmbiguityFeedbackIcon(@DrawableRes int mAmbiguityFeedbackIcon)
    {
        this.mAmbiguityFeedbackIcon = mAmbiguityFeedbackIcon;
        return this;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public CustomTripDialog setBackgroundColor(@ColorRes int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        return this;
    }

    public int getIconColor()
    {
        return mIconColor;
    }

    public CustomTripDialog setIconColor(@ColorRes int mIconColor)
    {
        this.mIconColor = mIconColor;
        return this;
    }

    public int getReviewQuestion()
    {
        return mReviewQuestion;
    }

    public CustomTripDialog setReviewQuestion(int mReviewQuestion)
    {
        this.mReviewQuestion = mReviewQuestion;
        return this;
    }

    public CustomTripDialog setOnReviewClickListener(FeedBackActionsListeners reviewActionsListeners)
    {
        this.mReviewActionsListener = reviewActionsListeners;
        return this;
    }

    public void dismiss()
    {
        if(mDialog != null)
        {
            mDialog.dismiss();
        }
    }

    private void onPositiveFeedbackClicked(View view)
    {
        if(mReviewActionsListener != null)
        {
            mReviewActionsListener.onPositiveFeedback(this);
        }
    }

    private void onNegativeFeedbackClicked(View view)
    {
        if(mReviewActionsListener != null)
        {
            mReviewActionsListener.onNegativeFeedback(this);
        }
    }

    private void onAmbiguityFeedbackClicked(View view)
    {
        if(mReviewActionsListener != null)
        {
            mReviewActionsListener.onAmbiguityFeedback(this);
        }
    }

    private void onCancelListener(DialogInterface dialog)
    {
        if (mReviewActionsListener != null)
        {
            mReviewActionsListener.onCancelListener(dialog);
        }
    }
}


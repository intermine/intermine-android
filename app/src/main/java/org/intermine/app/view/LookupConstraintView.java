package org.intermine.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import org.intermine.app.R;
import org.intermine.app.core.templates.constraint.PathConstraint;
import org.intermine.app.core.templates.constraint.PathConstraintLookup;
import org.intermine.app.util.Strs;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class LookupConstraintView extends ConstraintView {
    @InjectView(R.id.lookup_value)
    EditText mValue;

    public LookupConstraintView(Context context, PathConstraintLookup constraint) {
        super(context, constraint);

        init();
        mValue.setText(constraint.getValue());
    }

    public LookupConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LookupConstraintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.lookup_constraint_view, this);
        ButterKnife.inject(this);
    }

    @Override
    public boolean isValidConstraint() {
        return !Strs.isNullOrEmpty(mValue.getText().toString());
    }

    @Override
    public void highlightInvalid() {
        mValue.setError(getContext().getString(R.string.empty_value_em));
    }

    public String getValue() {
        return mValue.getText().toString();
    }

    @Override
    public PathConstraint getGeneratedConstraint() {
        PathConstraintLookup constraint = (PathConstraintLookup) getPathConstraint();
        return new PathConstraintLookup(constraint.getPath(), getValue(),
                null, constraint.getCode());
    }
}
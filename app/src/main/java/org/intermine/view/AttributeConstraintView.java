package org.intermine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.intermine.R;
import org.intermine.adapter.SimpleAdapter;
import org.intermine.core.templates.constraint.ConstraintOperation;
import org.intermine.core.templates.constraint.PathConstraint;
import org.intermine.core.templates.constraint.PathConstraintAttribute;
import org.intermine.util.Strs;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class AttributeConstraintView extends ConstraintView {
    @InjectView(R.id.operations_spinner)
    Spinner mOperationsSpinner;

    @InjectView(R.id.base_constraint_value)
    EditText mValue;

    @InjectView(R.id.constraint_path)
    TextView mPath;

    private PathConstraintAttribute mConstraint;

    public AttributeConstraintView(Context context, PathConstraintAttribute constraint) {
        super(context, constraint);
        mConstraint = constraint;

        init();
    }

    public AttributeConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AttributeConstraintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public boolean isValidConstraint() {
        return !Strs.isNullOrEmpty(mValue.getText().toString());
    }

    @Override
    public void highlightInvalid() {
        mValue.setError(getContext().getString(R.string.empty_value_em));
    }

    private void init() {
        inflate(getContext(), R.layout.attribute_constraint_view, this);
        ButterKnife.inject(this);

        SimpleAdapter<ConstraintOperation> adapter = new SimpleAdapter(getContext());
        List<ConstraintOperation> data = new ArrayList<>(PathConstraintAttribute.VALID_OPERATIONS);
        adapter.updateData(data);
        mOperationsSpinner.setAdapter(adapter);

        if (null != mConstraint) {
            mValue.setText(mConstraint.getValue());
            mPath.setText(mConstraint.getPath());

            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).equals(mConstraint.getOperation())) {
                    mOperationsSpinner.setSelection(i);
                }
            }
        }
    }

    public String getValue() {
        return mValue.getText().toString();
    }

    public ConstraintOperation getOperation() {
        return (ConstraintOperation) mOperationsSpinner.getSelectedItem();
    }

    @Override
    public PathConstraint getGeneratedConstraint() {
        PathConstraintAttribute constraint = (PathConstraintAttribute) getPathConstraint();
        return new PathConstraintAttribute(constraint.getPath(), getOperation(), getValue(),
                constraint.getCode());
    }
}

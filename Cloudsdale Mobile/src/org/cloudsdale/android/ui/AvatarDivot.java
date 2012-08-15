package org.cloudsdale.android.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.cloudsdale.android.R;

public class AvatarDivot extends ImageView implements Divot {

	private Drawable	drawable;
	private int			drawableIntrinsicWidth;
	private int			drawableIntrinsicHeight;
	private int			position;
	private float		density;

	public AvatarDivot(Context context) {
		super(context);
		initialize(null);
	}

	public AvatarDivot(Context context, AttributeSet attrs) {
		super(context);
		initialize(attrs);
	}

	public AvatarDivot(Context context, AttributeSet attrs, int defStyle) {
		super(context);
		initialize(attrs);
	}

	public void initialize(AttributeSet attrs) {
		if (attrs != null) {
			position = attrs.getAttributeListValue(null, "position",
					sPositionChoices, -1);
		}

		Resources r = getContext().getResources();
		density = r.getDisplayMetrics().density;

		setDrawable();
	}

	private void setDrawable() {
		Resources r = getContext().getResources();

		switch (position) {
			case LEFT_UPPER:
			case LEFT_MIDDLE:
			case LEFT_LOWER:
				drawable = r.getDrawable(R.drawable.msg_bubble_right);
				break;

			case RIGHT_UPPER:
			case RIGHT_MIDDLE:
			case RIGHT_LOWER:
				drawable = r.getDrawable(R.drawable.msg_bubble_left);
				break;

		// case TOP_LEFT:
		// case TOP_MIDDLE:
		// case TOP_RIGHT:
		// mDrawable = r.getDrawable(R.drawable.msg_bubble_bottom);
		// break;
		//
		// case BOTTOM_LEFT:
		// case BOTTOM_MIDDLE:
		// case BOTTOM_RIGHT:
		// mDrawable = r.getDrawable(R.drawable.msg_bubble_top);
		// break;
		}
		drawableIntrinsicWidth = drawable.getIntrinsicWidth();
		drawableIntrinsicHeight = drawable.getIntrinsicHeight();
	}

	@Override
	public void setPosition(int position) {
		this.position = position;
		setDrawable();
		invalidate();
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public float getCloseOffset() {
		return CORNER_OFFSET * density;
	}

	@Override
	public float getFarOffset() {
		return getCloseOffset() + drawableIntrinsicHeight;
	}

	@Override
	public ImageView asImageView() {
		return this;
	}

	@Override
	public void assignContactFromEmail(String emailAddress) {
		// Stub, not used
	}

	private void computeBounds(Canvas c) {
		final int left = 0;
		final int top = 0;
		final int right = getWidth();
		final int middle = right / 2;
		final int bottom = getHeight();

		final int cornerOffset = (int) getCloseOffset();

		switch (position) {
			case RIGHT_UPPER:
				drawable.setBounds(right - drawableIntrinsicWidth, top
						+ cornerOffset, right, top + cornerOffset
						+ drawableIntrinsicHeight);
				break;

			case LEFT_UPPER:
				drawable.setBounds(left, top + cornerOffset, left
						+ drawableIntrinsicWidth, top + cornerOffset
						+ drawableIntrinsicHeight);
				break;

			case BOTTOM_MIDDLE:
				int halfWidth = drawableIntrinsicWidth / 2;
				drawable.setBounds((int) (middle - halfWidth),
						(int) (bottom - drawableIntrinsicHeight),
						(int) (middle + halfWidth), (int) (bottom));

				break;
		}
	}

	@Override
	public void onDraw(Canvas c) {
		super.onDraw(c);
		c.save();
		computeBounds(c);
		drawable.draw(c);
		c.restore();
	}
}

package com.vegbags.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vegbags.R;
import com.vegbags.ResponseModels.AddressModel;
import com.vegbags.utils.CommonUtils;

import java.util.List;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private List<AddressModel> addressModelList;
    private AddressRecyclerInterface addressRecyclerInterface;
    private boolean isAddress;
    private static final String TAG = "AddressAdapter";
    private int checkedPosition = -1;
    private boolean isBilling;

    public AddressAdapter(List<AddressModel> addressModelList, AddressRecyclerInterface addressRecyclerInterface) {
        this.addressModelList = addressModelList;
        this.addressRecyclerInterface = addressRecyclerInterface;
    }

    public AddressAdapter(List<AddressModel> addressModelList, boolean isAddress, boolean isBilling) {
        this.addressModelList = addressModelList;
        this.isAddress = isAddress;
        this.isBilling = isBilling;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isAddress) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.item_address, parent, false);
            AddressAdapter.MyViewHolder viewHolder = new AddressAdapter.MyViewHolder(listItem);
            return viewHolder;
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.address_item, parent, false);
            AddressAdapter.MyViewHolder viewHolder = new AddressAdapter.MyViewHolder(listItem);
            return viewHolder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (isAddress) {

            if (position == 0) {
                holder.checkBox.setChecked(true);
            } else holder.checkBox.setChecked(false);

            holder.contactName.setText(addressModelList.get(position).getContactName());
            holder.address.setText(addressModelList.get(position).getAddress());
            holder.addressPhone.setText(addressModelList.get(position).getPhoneNo());
            holder.addressPinCode.setText(addressModelList.get(position).getPincode());

            if (addressModelList.get(position).getAltPhoneNo() != null) {
                holder.addressAltPhone.setText(addressModelList.get(position).getAltPhoneNo());
                holder.llAltPhone.setVisibility(View.VISIBLE);
            } else {
                holder.llAltPhone.setVisibility(View.GONE);
            }

            holder.bind(addressModelList.get(position));


        } else {

            holder.addressId.setText(addressModelList.get(position).getId().toString());
            holder.contactName.setText(addressModelList.get(position).getContactName());
            holder.address.setText(addressModelList.get(position).getAddress());
            holder.addressPhone.setText(addressModelList.get(position).getPhoneNo());
            holder.addressPinCode.setText(addressModelList.get(position).getPincode());

            if (addressModelList.get(position).getAltPhoneNo() != null) {
                holder.addressAltPhone.setText(addressModelList.get(position).getAltPhoneNo());
                holder.llAltPhone.setVisibility(View.VISIBLE);
            } else {
                holder.llAltPhone.setVisibility(View.GONE);
            }

            AddressModel addressModel = addressModelList.get(position);


            holder.llDelete.setOnClickListener(view -> addressRecyclerInterface.onDeleteClick(addressModel.getId()));

            holder.llUpdate.setOnClickListener(view -> addressRecyclerInterface.onUpdateClick(addressModel));
        }


    }

    @Override
    public int getItemCount() {
        if (addressModelList != null)
            return addressModelList.size();
        else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView addressId, address, addressPinCode, addressPhone, addressAltPhone, contactName;
        LinearLayout llAltPhone, llDelete, llUpdate;
        RadioButton checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            llAltPhone = itemView.findViewById(R.id.llAltPhone);
            addressId = itemView.findViewById(R.id.addressId);
            contactName = itemView.findViewById(R.id.contactName);
            address = itemView.findViewById(R.id.address);
            addressPinCode = itemView.findViewById(R.id.addressPinCode);
            addressPhone = itemView.findViewById(R.id.addressPhone);
            addressAltPhone = itemView.findViewById(R.id.addressAltPhone);
            llDelete = itemView.findViewById(R.id.llDelete);
            llUpdate = itemView.findViewById(R.id.llUpdate);
            checkBox = itemView.findViewById(R.id.rview_radiobutton);


        }

        void bind(final AddressModel addressModel) {
            if (checkedPosition == -1) {
                checkBox.setChecked(false);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }

            itemView.setOnClickListener(view -> {
                checkBox.setChecked(true);
                if (checkedPosition != getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();

                    if (isBilling)
                        CommonUtils.setBillingAddress(addressModel.getId());
                    else
                        CommonUtils.setBillingAddress(addressModel.getId());
                    Log.d(TAG, "bind: " + addressModel.getId());
                }
            });
        }
    }

}

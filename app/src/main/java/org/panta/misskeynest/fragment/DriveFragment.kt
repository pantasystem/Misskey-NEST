package org.panta.misskeynest.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.entity.ConnectionProperty

enum class ViewType{
    FOLDER, FILE
}
class DriveFragment : Fragment(){

    companion object{
        private const val EXTRA_VIEW_TYPE = "DriveFragmentConnectionProperty"
        fun getInstance(connectionProperty: ConnectionProperty, viewType: ViewType): DriveFragment{
            return DriveFragment().apply{
                arguments = Bundle().apply{
                    putSerializable(EXTRA_VIEW_TYPE, viewType.ordinal)
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //mConnectionProperty = PersonalRepository(SharedPreferenceOperator(context!!)).getConnectionInfo()
        return inflater.inflate(R.layout.fragment_drive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connectionProperty = arguments?.getSerializable(EXTRA_VIEW_TYPE) as ConnectionProperty



    }
}
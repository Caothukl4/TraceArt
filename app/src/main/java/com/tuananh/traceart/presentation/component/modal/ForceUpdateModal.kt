package com.tuananh.traceart.presentation.component.modal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tuananh.traceart.R
import com.tuananh.traceart.presentation.component.button.Button
import com.tuananh.traceart.presentation.component.button.ButtonType
import com.tuananh.traceart.presentation.theme.AppTypography
import com.tuananh.traceart.utils.gotoGooglePlayStore

@Composable
fun ForceUpdateModal(showModal: Boolean, onDismiss: () -> Unit = {}) {
    val context = LocalContext.current
    CenterModal(showModal = showModal, onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.Update_Required),
                style = AppTypography.titleMedium
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.Update_description),
                style = AppTypography.labelMedium
            )
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    text = stringResource(R.string.cancel),
                    type = ButtonType.Linking,
                    onClick = { onDismiss() })
                Button(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    text = stringResource(R.string.Update),
                    type = ButtonType.Primary,
                    onClick = { context.gotoGooglePlayStore() })

            }
        }

    }
}
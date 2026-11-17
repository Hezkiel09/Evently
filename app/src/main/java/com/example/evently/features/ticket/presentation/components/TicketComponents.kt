package com.example.evently.features.ticket.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evently.features.ticket.data.EventTicketItem
import com.example.evently.features.ticket.data.TicketStatus
import com.example.evently.features.ticket.data.UserTicket

val EventlyOrange = Color(0xFFF39200)
val EventlyOrangeLight = Color(0xFFFFF4E5)
val EventlyDarkText = Color(0xFF18181B)
val EventlyMutedText = Color(0xFF71717A)
val EventlySurfaceGray = Color(0xFFF4F4F5)
val EventlyBorderGray = Color(0xFFE4E4E7)
val EventlySuccessGreen = Color(0xFF15803D)
val EventlySuccessBg = Color(0xFFDCFCE7)

@Composable
fun EventlyLocationIcon(
    tint: Color,
    modifier: Modifier = Modifier.size(14.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.38f
        val r = w * 0.28f

        val pinPath = Path().apply {
            moveTo(cx - r, cy)
            cubicTo(cx - r, cy - r * 1.1f, cx + r, cy - r * 1.1f, cx + r, cy)
            cubicTo(cx + r, cy + r * 0.9f, cx, h * 0.92f, cx, h * 0.92f)
            cubicTo(cx, h * 0.92f, cx - r, cy + r * 0.9f, cx - r, cy)
            close()
        }
        drawPath(
            path = pinPath,
            color = tint,
            style = Stroke(width = 1.6.dp.toPx(), join = StrokeJoin.Round)
        )
        drawCircle(
            color = tint,
            radius = r * 0.35f,
            center = Offset(cx, cy)
        )
    }
}

@Composable
fun EventlyClockIcon(
    tint: Color,
    modifier: Modifier = Modifier.size(14.dp)
) {
    Canvas(modifier = modifier) {
        val strokeW = 1.5.dp.toPx()
        val center = Offset(size.width * 0.5f, size.height * 0.5f)
        drawCircle(
            color = tint,
            radius = size.minDimension * 0.42f,
            center = center,
            style = Stroke(width = strokeW)
        )
        drawLine(
            color = tint,
            start = center,
            end = Offset(size.width * 0.5f, size.height * 0.26f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = center,
            end = Offset(size.width * 0.68f, size.height * 0.5f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun EventlyCheckCircleIcon(
    tint: Color,
    modifier: Modifier = Modifier.size(14.dp)
) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = tint,
            radius = size.minDimension * 0.45f,
            center = Offset(size.width * 0.5f, size.height * 0.5f)
        )
        val checkPath = Path().apply {
            moveTo(size.width * 0.28f, size.height * 0.52f)
            lineTo(size.width * 0.44f, size.height * 0.67f)
            lineTo(size.width * 0.72f, size.height * 0.35f)
        }
        drawPath(
            path = checkPath,
            color = Color.White,
            style = Stroke(
                width = 1.7.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
fun EventlySearchIcon(
    tint: Color,
    modifier: Modifier = Modifier.size(20.dp)
) {
    Canvas(modifier = modifier) {
        val strokeW = 2.dp.toPx()
        val center = Offset(size.width * 0.44f, size.height * 0.44f)
        drawCircle(
            color = tint,
            radius = size.minDimension * 0.28f,
            center = center,
            style = Stroke(width = strokeW)
        )
        drawLine(
            color = tint,
            start = Offset(size.width * 0.65f, size.height * 0.65f),
            end = Offset(size.width * 0.85f, size.height * 0.85f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun EventlyCloseIcon(
    tint: Color,
    modifier: Modifier = Modifier.size(20.dp)
) {
    Canvas(modifier = modifier) {
        val strokeW = 2.dp.toPx()
        drawLine(
            color = tint,
            start = Offset(size.width * 0.28f, size.height * 0.28f),
            end = Offset(size.width * 0.72f, size.height * 0.72f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(size.width * 0.72f, size.height * 0.28f),
            end = Offset(size.width * 0.28f, size.height * 0.72f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun EventlyBackIcon(
    tint: Color,
    modifier: Modifier = Modifier.size(22.dp)
) {
    Canvas(modifier = modifier) {
        val strokeW = 2.2.dp.toPx()
        drawLine(
            color = tint,
            start = Offset(size.width * 0.24f, size.height * 0.5f),
            end = Offset(size.width * 0.80f, size.height * 0.5f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        val chevron = Path().apply {
            moveTo(size.width * 0.46f, size.height * 0.26f)
            lineTo(size.width * 0.22f, size.height * 0.5f)
            lineTo(size.width * 0.46f, size.height * 0.74f)
        }
        drawPath(
            path = chevron,
            color = tint,
            style = Stroke(
                width = strokeW,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
fun EventCatalogCard(
    event: EventTicketItem,
    isRegistered: Boolean,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, EventlyBorderGray),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 92.dp, height = 104.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF2E1065), Color(0xFFF39200))
                        )
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.45f)
                ) {
                    Text(
                        text = event.category,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = event.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = EventlyDarkText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    EventlyLocationIcon(
                        tint = EventlyMutedText,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.location,
                        fontSize = 11.sp,
                        color = EventlyMutedText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    EventlyClockIcon(
                        tint = EventlyMutedText,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${event.timeRange} • Sisa ${event.quotaRemaining} kuota",
                        fontSize = 11.sp,
                        color = EventlyMutedText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.priceLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EventlyOrange
                    )

                    if (isRegistered) {
                        OutlinedButton(
                            onClick = onActionClick,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, EventlySuccessGreen),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            EventlyCheckCircleIcon(
                                tint = EventlySuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Lihat Tiket",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EventlySuccessGreen
                            )
                        }
                    } else {
                        Button(
                            onClick = onActionClick,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EventlyOrange),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = "Daftar",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DigitalTicketCard(
    ticket: UserTicket,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isUpcoming = ticket.status == TicketStatus.UPCOMING
    val badgeBg = when (ticket.status) {
        TicketStatus.UPCOMING -> EventlyOrangeLight
        TicketStatus.COMPLETED -> EventlySuccessBg
        TicketStatus.CANCELLED -> Color(0xFFFEE2E2)
    }
    val badgeText = when (ticket.status) {
        TicketStatus.UPCOMING -> EventlyOrange
        TicketStatus.COMPLETED -> EventlySuccessGreen
        TicketStatus.CANCELLED -> Color(0xFFDC2626)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(
            width = if (isUpcoming) 1.5.dp else 1.dp,
            color = if (isUpcoming) EventlyOrange.copy(alpha = 0.5f) else EventlyBorderGray
        ),
        shadowElevation = if (isUpcoming) 3.dp else 0.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = badgeBg
                        ) {
                            Text(
                                text = ticket.status.label,
                                color = badgeText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = EventlySurfaceGray
                        ) {
                            Text(
                                text = ticket.category,
                                color = EventlyDarkText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = ticket.ticketCode,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EventlyMutedText
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = ticket.timeRange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = EventlyMutedText
                )

                Text(
                    text = ticket.eventTitle,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = EventlyDarkText,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        EventlyLocationIcon(
                            tint = EventlyOrange,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = ticket.location,
                            fontSize = 12.sp,
                            color = EventlyMutedText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = ticket.date,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = EventlyDarkText
                    )
                }
            }

            TicketPerforationDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAFAFA))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pemegang: ${ticket.holderName}",
                    fontSize = 12.sp,
                    color = EventlyMutedText
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TicketQrCodeCanvas(
                        ticketCode = ticket.ticketCode,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isUpcoming) "Tampilkan QR" else "Detail Tiket",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUpcoming) EventlyOrange else EventlyMutedText
                    )
                }
            }
        }
    }
}

@Composable
fun TicketPerforationDivider(
    notchColor: Color = Color(0xFFF8F9FA)
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        val notchRadius = 10.dp.toPx()
        val yCenter = size.height / 2f

        drawLine(
            color = EventlyBorderGray,
            start = Offset(notchRadius + 8f, yCenter),
            end = Offset(size.width - notchRadius - 8f, yCenter),
            strokeWidth = 1.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
        )

        drawCircle(
            color = notchColor,
            radius = notchRadius,
            center = Offset(0f, yCenter)
        )
        drawCircle(
            color = notchColor,
            radius = notchRadius,
            center = Offset(size.width, yCenter)
        )
    }
}

@Composable
fun TicketQrCodeCanvas(
    ticketCode: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val gridSize = 21
        val cellSize = size.width / gridSize
        val seed = ticketCode.hashCode()

        fun isFinderPattern(r: Int, c: Int): Boolean {
            val topLeft = r < 7 && c < 7
            val topRight = r < 7 && c >= gridSize - 7
            val bottomLeft = r >= gridSize - 7 && c < 7
            return topLeft || topRight || bottomLeft
        }

        fun isFinderFilled(r: Int, c: Int): Boolean {
            val localR = when {
                r < 7 -> r
                r >= gridSize - 7 -> r - (gridSize - 7)
                else -> 0
            }
            val localC = when {
                c < 7 -> c
                c >= gridSize - 7 -> c - (gridSize - 7)
                else -> 0
            }
            return localR == 0 || localR == 6 || localC == 0 || localC == 6 ||
                    (localR in 2..4 && localC in 2..4)
        }

        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val filled = if (isFinderPattern(r, c)) {
                    isFinderFilled(r, c)
                } else {
                    ((r * 31 + c * 17 + seed) and 3) == 0 || ((r + c + seed) % 3 == 0)
                }
                if (filled) {
                    drawRect(
                        color = EventlyDarkText,
                        topLeft = Offset(c * cellSize, r * cellSize),
                        size = Size(cellSize, cellSize)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationConfirmationSheet(
    event: EventTicketItem,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var attendeeName by remember { mutableStateOf("Mahasiswa Evently") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Konfirmasi Pendaftaran Tiket",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = EventlyDarkText
            )

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = EventlySurfaceGray,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = event.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = EventlyDarkText
                    )
                    Text(
                        text = "${event.dateGroup} • ${event.timeRange}",
                        fontSize = 13.sp,
                        color = EventlyMutedText
                    )
                    Text(
                        text = event.location,
                        fontSize = 13.sp,
                        color = EventlyMutedText
                    )
                }
            }

            OutlinedTextField(
                value = attendeeName,
                onValueChange = { attendeeName = it },
                label = { Text("Nama Lengkap Peserta") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = EventlyDarkText,
                    unfocusedTextColor = EventlyDarkText,
                    cursorColor = EventlyOrange,
                    focusedBorderColor = EventlyOrange,
                    unfocusedBorderColor = EventlyBorderGray,
                    focusedLabelColor = EventlyOrange,
                    unfocusedLabelColor = EventlyMutedText,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Batal", color = EventlyDarkText)
                }

                Button(
                    onClick = { onConfirm(attendeeName) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EventlyOrange),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Klaim Tiket",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketQrDetailSheet(
    ticket: UserTicket,
    onDismiss: () -> Unit,
    onCancelTicket: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "E-Ticket Evently",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = EventlyDarkText
            )

            Text(
                text = "Tunjukkan QR Code ini kepada panitia saat check-in",
                fontSize = 12.sp,
                color = EventlyMutedText
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.5.dp, EventlyBorderGray),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TicketQrCodeCanvas(
                        ticketCode = ticket.ticketCode,
                        modifier = Modifier.size(170.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = ticket.ticketCode,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EventlyDarkText,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = ticket.eventTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = EventlyDarkText
                )
                Text(
                    text = "Jadwal: ${ticket.date} (${ticket.timeRange})",
                    fontSize = 13.sp,
                    color = EventlyMutedText
                )
                Text(
                    text = "Lokasi: ${ticket.location}",
                    fontSize = 13.sp,
                    color = EventlyMutedText
                )
                Text(
                    text = "Peserta: ${ticket.holderName}",
                    fontSize = 13.sp,
                    color = EventlyMutedText
                )
            }

            if (ticket.status == TicketStatus.UPCOMING) {
                TextButton(
                    onClick = { onCancelTicket(ticket.ticketCode) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Batalkan Pendaftaran Tiket",
                        color = Color(0xFFDC2626),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}